package oop.project.cli.argparser;

import java.util.ArrayList;

import static oop.project.cli.argparser.ArgToken.Type.POSITIONAL_ARG;

public class Validator {

    /**
     * Validates the tokens with the arguments currently in the namespace. I.e., checks their types, ranges,
     *
     * @param tokens List of tokens, as generated from lex.
     * @param arguments List of arguments defined for the parser.
     * @throws ValidationException if validation fails.
     */
    static MappedData data = null;
    public static void validate(ArrayList<ArgToken> tokens, ArrayList<Argument> arguments, MappedData _data) throws ValidationException {
        data = _data;
        for (ArgToken token : tokens) {
            Argument argument = findArgument(token.name(), arguments);
            if (argument == null) {
                throw new ValidationException("Unknown argument: " + token.name());
            }
            validateToken(token, argument);
        }
    }

    /**
     * Finds the argument with the given name in the list of arguments.
     *
     * @param name Name of the argument to find.
     * @param arguments List of arguments to search in.
     * @return The argument with the given name, or null if not found.
     */
    private static Argument findArgument(String name, ArrayList<Argument> arguments) {
        for (var argument : arguments) {
            if(argument.ref.equals(name)){
                return argument;
            }
            for(var currName : argument.names) {
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
     * @param argument The corresponding argument.
     * @throws ValidationException if validation fails.
     */
    private static void validateToken(ArgToken token, Argument argument) throws ValidationException {
        // Perform validation based on the type of argument
        if(argument.positional){
            validatePositionalArgument(token, argument);
        }
        else{
            validateNamedArgument(token, argument);
        }
    }

    private static void validatePositionalArgument(ArgToken token, Argument argument) throws ValidationException {
        throw new NotImplementedException("validate positional not done");

    }

    private static void validateNamedArgument(ArgToken token, Argument argument) throws ValidationException {
        if(argument.ref.equals("help")){
            invokeHelp();
        }
        throw new NotImplementedException("validate named not done");
    }

    private static void validateFlag(ArgToken token, Argument argument) throws ValidationException {
        throw new NotImplementedException("validate flag not done");
    }

    private static void invokeHelp() {
        System.out.print("Usage: " );
        StringBuilder positionals = new StringBuilder();
        int optional = 0;
        for(var entry : data.map.entrySet()){
            var value = entry.getValue();
            if(value.positional){
                var name = value.helpName == null ? value.ref : value.helpName;
                positionals.append("[").append(name).append("] ");
            }else{
                if(!value.required){
                    optional++;
                    continue;
                }
                var name = value.helpName == null ? value.names[0] : value.helpName;
                System.out.print("< " + name + " > ");
            }
        }
        if(optional > 0) System.out.print("< options > ");
        System.out.println(positionals + "\nOptions:");

        for(var entry : data.map.entrySet()) {
            var value = entry.getValue();
            if(!value.positional && !value.ref.equals("help")){
                for (int i = 0; i < value.names.length; i++) {
                    System.out.print(value.names[i]);
                    if (i < value.names.length - 1) System.out.print(", ");
                }
                if(value.helpMessage != null)
                    System.out.print("\t\t" + value.helpMessage);
                System.out.println();
            }
        }
    }
}
