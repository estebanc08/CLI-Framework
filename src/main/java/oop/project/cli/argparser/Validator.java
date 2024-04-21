package oop.project.cli.argparser;

import java.util.ArrayList;
import java.util.HashSet;

import static oop.project.cli.argparser.ArgToken.Type.NAMED_ARG;
import static oop.project.cli.argparser.ArgToken.Type.POSITIONAL_ARG;

public class Validator {

    ArrayList<Argument<Object>> arguments;
    HashSet<String> consumedArguments;

    public Validator(ArrayList<Argument<Object>> arguments) {
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
        for (ArgToken token : tokens) {
            validateToken(token);
        }
    }

    /**
     * Finds the argument with the given name in the list of arguments.
     *
     * @param name Name of the argument to find.
     * @return The argument with the given name, or null if not found.
     */
    private Argument<Object> findArgument(String name) {
        for (var argument : arguments) {
            if (argument.names.contains(name)) {
                return argument;
            }
            for (var currName : argument.names) {
                if (currName.equals(name)) {
                    return argument;
                }
            }
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
        if (findArgument(name) == null) { throw new ValidationException("No such argument " + name + " found."); }
        consumedArguments.add(name);



        throw new NotImplementedException("validate named not done");
    }

    private static void validateFlag(ArgToken token, Argument argument) throws ValidationException {
        throw new NotImplementedException("validate flag not done");
    }
}
