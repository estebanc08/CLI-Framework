package oop.project.cli.argparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static oop.project.cli.argparser.ArgToken.Type.NAMED_ARG;
import static oop.project.cli.argparser.ArgToken.Type.POSITIONAL_ARG;

public class Validator {

    ArrayList<Argument<?>> arguments;
    HashSet<String> consumedArguments;

    public Validator(ArrayList<Argument<?>> arguments) {
        this.arguments = arguments;
        this.consumedArguments = new HashSet<>();
    }

    /**
     * Validates the tokens with the arguments currently in the namespace.
     *
     * @param tokens List of tokens, as generated from lex.
     * @throws ValidationException if validation fails.
     */
    public void validate(ArrayList<ArgToken> tokens) throws ValidationException {
        // Validate all tokens
        for (ArgToken token : tokens) { validateToken(token); }

        // Once tokens are validated, we can start assigning our arguments values
        for (ArgToken token : tokens) {
            var argument = findArgument(token.name());
            assert(argument != null);  // Likely would never happen but just to appease the compiler
            argument.value = token.value();
        }
    }

    /**
     * Finds the argument with the given name in the list of arguments.
     *
     * @param name Name of the argument to find.
     * @return The argument with the given name, or null if not found.
     */
    private Argument<?> findArgument(String name) {
        for (var arg : arguments) {
            if (Arrays.asList(arg.names).contains(name)) { return arg; }
        }
        return null;
    }

    /**
     * Validates a token against the corresponding argument.
     *
     * @param token The token to validate.
     * @throws ValidationException if validation fails.
     */
    private void validateToken(ArgToken token) throws ValidationException {
        // Perform validation based on the type of argument
        if (token.type() == POSITIONAL_ARG) { validatePositionalArgument(token); }
        else if (token.type() == NAMED_ARG) { validateNamedArgument(token); }
        else { throw new NotImplementedException("Token of type " + token.type() + " has not been implemented yet."); }
    }

    private void validatePositionalArgument(ArgToken token) throws ValidationException {
        throw new NotImplementedException("validate positional not done");
    }

    private void validateNamedArgument(ArgToken token) throws ValidationException {
        // Store and verify that this token's name is valid
        String name = token.name();
        if (consumedArguments.contains(name)) { throw new ValidationException("Duplicate definition for " + name + "."); }

        // Get the argument, verify that it exists
        var argument = findArgument(name);
        if (argument == null) { throw new ValidationException("No such argument " + name + " found."); }


        // Type validation
        var type = argument.type;
        if (!token.value().getFirst().getClass().equals(argument.type))
            { throw new ValidationException("Got type " + token.value().getFirst().getClass() + ", expected " + argument.type.toString()); }

        // Validate its range
        validateObjectRange(argument);

        consumedArguments.add(name);
    }

    private <U extends Comparable<U>> void validateObjectRange(Argument<U> argument) throws ValidationException {
        for (var val : argument.value) {
            if (!argument.range.isInRange((U) val))
            { throw new ValidationException(val + " is not in range" + argument.range.toString()); }
        }
    }

    private static void validateFlag(ArgToken token, Argument argument) throws ValidationException {
        throw new NotImplementedException("validate flag not done");
    }
}
