package oop.project.cli.argparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static oop.project.cli.argparser.ArgToken.Type.*;

public class Validator {
    ArrayList<Argument<?>> tempArguments;  // for use as a transaction - either it all completes, or none of it
    ArrayList<Argument<?>> argumentsReference;
    HashSet<String> consumedArguments;

    public Validator(ArrayList<Argument<?>> arguments) {
        this.argumentsReference = arguments;

        this.tempArguments = new ArrayList<>();
        this.tempArguments.addAll(arguments);  // deep copy

        this.consumedArguments = new HashSet<>();
    }

    /**
     * Validates the tokens with the arguments currently in the namespace.
     *
     * @param tokens List of tokens, as generated from lex.
     * @throws ArgParseException if validation fails.
     */
    public void validate(ArrayList<ArgToken> tokens) throws ArgParseException {
        // Validate all tokens
        for (ArgToken token : tokens) { validateToken(token); }

        // Make sure stuff that's required is all accounted for
        if (tempArguments.stream().anyMatch(argument -> argument.required && argument.value.isEmpty()))
            { throw  new ArgParseException("Required argument not present."); }

        // Once tokens are validated, we can start assigning our arguments values
        argumentsReference = tempArguments;
    }

    /**
     * Finds the argument with the given name in the list of arguments.
     *
     * @param name Name of the argument to find.
     * @return The argument with the given name, or null if not found.
     */
    private Argument<?> findArgument(String name) {
        for (var arg : tempArguments) {
            if (Arrays.asList(arg.names).contains(name)) { return arg; }
        }
        return null;
    }

    /**
     * Validates a token against the corresponding argument.
     *
     * @param token The token to validate.
     * @throws ArgParseException if validation fails.
     */
    private void validateToken(ArgToken token) throws ArgParseException {
        // Perform validation based on the type of argument
        if (token.type() == POSITIONAL_ARG) { validatePositionalArgument(token); }
        else if (token.type() == NAMED_ARG) { validateNamedArgument(token); }
        else if(token.type() == FLAG) { validateFlag(token);}
        else { throw new NotImplementedException("Token of type " + token.type() + " has not been implemented yet."); }
    }

    private void validatePositionalArgument(ArgToken token) throws ArgParseException {
        // Get the first argument that fits the criteria and is required
        Optional<Argument<?>> argument = tempArguments.stream()
                .filter(arg -> !consumedArguments.contains(arg.ref)
                        && arg.positional
                        && arg.required
                        && checkNArgs(token.value().size(), arg.nArgs)
                        && token.value().getFirst().getClass().equals(arg.type))
                .findFirst();

        // We found a match!
        if (argument.isPresent()) {
            validateObjectRange(argument.get());
            consumedArguments.add(argument.get().ref);
            argument.get().value = token.value();
        }
        // No such *required* argument? Let's look if there's a non-required one
        else {
            argument = tempArguments.stream()
                    .filter(arg -> !consumedArguments.contains(arg.ref)
                            && arg.positional
                            && checkNArgs(token.value().size(), arg.nArgs)
                            && token.value().getFirst().getClass().equals(arg.type))
                    .findFirst();
            // We found a match!
            if (argument.isPresent()) {
                validateObjectRange(argument.get());
                consumedArguments.add(argument.get().ref);
                argument.get().value = token.value();
            }
            // Maybe it's the number of arguments...?
            else {
                if (tempArguments.stream().anyMatch(arg -> !consumedArguments.contains(arg.ref)
                                && arg.positional
                                && token.value().getFirst().getClass().equals(arg.type)
                                && !checkNArgs(token.value().size(), arg.nArgs)))
                    { throw new ArgParseException("Invalid number of arguments " + token.value().size() + "."); }
                else
                    { throw new ArgParseException("Unknown positional argument with type " + token.value().getFirst().getClass().getSimpleName()); }
            }
        }
    }

    private void validateNamedArgument(ArgToken token) throws ArgParseException {
        // Store and verify that this token's name is valid
        String name = token.name();
        if (consumedArguments.contains(name)) { throw new ArgParseException("Duplicate definition for " + name + "."); }

        // Get the argument, verify that it exists
        var argument = findArgument(name);
        if (argument == null) { throw new ArgParseException("No such argument " + name + " found."); }

        // Type validation
        for(var val : token.value()){
            if(!val.getClass().equals(argument.type))
            { throw new ArgParseException("Got type " + token.value().getFirst().getClass() + ", expected " + argument.type.toString()); }
        }

        // Validate its range
        validateObjectRange(argument);

        // Validate its nargs
        if (!checkNArgs(token.value().size(), argument.nArgs))
            { throw new ArgParseException("Invalid number of arguments " + token.value().size() + ", expected [" + argument.nArgs + "]"); }

        consumedArguments.add(name);
        argument.value = token.value();
    }

    private <U extends Comparable<U>> void validateObjectRange(Argument<U> argument) throws ArgParseException {
        for (var val : argument.value) {
            if (!argument.range.isInRange((U) val))
            { throw new ArgParseException(val + " is not in range" + argument.range.toString()); }
        }
    }

    private void validateFlag(ArgToken token) throws ArgParseException {
        var name = token.name();
        if(consumedArguments.contains(name)) { throw new ArgParseException("Duplicate definition for " + name + "."); }

        Argument<?> argument = null;
        for (var arg : tempArguments) {
            if (Arrays.asList(arg.names).contains(name)) {
                argument = arg;
                break;
            }
        }
        if (argument == null) { throw new ArgParseException("No such argument " + name + " found."); }
        for(var val : token.value()){
            if(!val.getClass().equals(argument.type))
            { throw new ArgParseException("Got type " + token.value().getFirst().getClass() + ", expected " + argument.type.toString()); }
        }

        // Validate its nargs
        if (!checkNArgs(token.value().size(), argument.nArgs))
            { throw new ArgParseException("Invalid number of arguments " + token.value().size() + ", expected [" + argument.nArgs + "]"); }

        consumedArguments.add(name);
        argument.value = token.value();
    }

    private static boolean checkNArgs(int length, String nArgs) throws ArgumentException {
        return switch (nArgs) {
            case "?" -> (length == 0 || length == 1);
            case "*" -> (length >= 0);
            case "+" -> (length >= 1);
            default -> {
                try { yield length == Integer.parseInt(nArgs); }
                catch (Exception e) { throw new ArgumentException("Unknown value of nArgs."); }
            }
        };
    }

}
