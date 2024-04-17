package oop.project.cli.argparser;

import java.util.ArrayList;

public class ArgumentParser {
    private final String programName;
    public String description;
    private static final ReturnValue namespace = new ReturnValue();

    public ArgumentParser(String programName, String description) {
        this.programName = programName;
        this.description = description;
    }

    /**
     * Adds an argument to the parser. This will add it to the namespace, but will not be given a value
     *  until parsing actually happens.
     *
     * @param argument Argument to add to the parser.
     */
    public void addArgument(Argument argument) {
        // TODO: Add some logic for duplicate refs, maybe throw an error?
        namespace.map.put(argument.ref, argument);
    }

    /**
     * Fetches argument from the namespace, or {@code null} if none exists.
     *
     * @param ref String referencing an argument in the namespace
     * @return Argument associated with the string, or {@code null} if none exists.
     */
    public Argument getArgument(String ref) {
        return namespace.map.get(ref);
    }


    // TODO: Better docstring once we figure out how this function is going to work.
    //  When I say "user" here, I mean "developer who is using this parsing library."
    //      Considerations:
    //          - Does it make sense for lex and validate to be separate methods?
    //              Could we not just have everything in the parse() method?
    //              From the user's point of view, nothing would change, since lex and validate
    //              are both private anyway
    //          -  How do we let the user know when something went wrong?
    //              Do we return a string, maybe an int, or maybe some custom return object?
    //          -   Do we catch the validation exceptions, or assume the user can catch those and deal with them
    //              how they wish?
    /**
     * Parses input.
     *
     * @param input Full command to be parsed.
     * @return Argument associated with the string, or {@code null} if none exists.
     */
    public static ReturnValue parse(String input) {
        // TODO: Full end-to-end parsing, calling lex and validate

        try {
            validate(lex(input));
        } catch (ValidationException err) {
            // TODO: Do something with it, tell the user somehow
        }

        return namespace;
    }

    /**
     * Lexing of the raw input into an array of tokens.
     *
     * @param rawArguments Raw arguments - that is, whatever the user input.
     * @return List of tokens, each with values, kinds, and potentially names (if non-positional: empty string if unnamed)
     */
    private static ArrayList<ArgToken> lex(String rawArguments) {
        // TODO: Lex

        ArrayList<String> wordsList = new ArrayList<>();
        boolean insideQuotes = false;
        String curr = "";
        for(int i = 0; i < rawArguments.length(); i++){
            if(rawArguments.charAt(i) == ' ' && !insideQuotes){
                wordsList.add(curr);
                curr = " ";
            }
            if(rawArguments.charAt(i) == '"'){
                insideQuotes = !insideQuotes;
            }
        }

        ArrayList<ArgToken> tokens = new ArrayList<>();
        for(int i = 0; i < wordsList.size(); i++){

        }




       return tokens;
    }

    /**
     * Validates the tokens with the arguments currently in the namespace. I.e., checks their types, ranges,
     *  number of args, etc. Throws a validation error if something is awry.
     * @param tokens List of tokens, as generated from lex.
     */
    private static void validate(ArrayList<ArgToken> tokens) throws ValidationException {
        // TODO: Validate
        throw new NotImplementedException("Validator not implemented");
        //throw new ValidationException("TODO: more descriptive error message here");
    }
}
