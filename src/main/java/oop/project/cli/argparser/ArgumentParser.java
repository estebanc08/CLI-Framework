package oop.project.cli.argparser;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgumentParser {
    private final String programName;
    public String description;
    private final HashMap<String, Argument> namespace;

    public ArgumentParser(String programName, String description) {
        this.programName = programName;
        this.description = description;
        this.namespace = new HashMap<>();
    }

    /**
     * Adds an argument to the parser. This will add it to the namespace, but will not be given a value
     *  until parsing actually happens.
     *
     * @param argument Argument to add to the parser.
     */
    public void addArgument(Argument argument) {
        // TODO: Add some logic for duplicate refs, maybe throw an error?
        namespace.put(argument.ref, argument);
    }

    /**
     * Fetches argument from the namespace, or {@code null} if none exists.
     *
     * @param ref String referencing an argument in the namespace
     * @return Argument associated with the string, or {@code null} if none exists.
     */
    public Argument getArgument(String ref) {
        return namespace.get(ref);
    }

    // TODO: Better docstring once we figure out how this function is going to work.
    //  When I say "user" here, I mean "developer who is using this parsing library."
    //      Considerations:
    //          - Does it make sense for lex and validate to be separate methods?
    //              Could we not just have everything in the parse() method?
    //              From the user's point of view, nothing would change, since lex and validate
    //              are both private anyway
    //          - What should this method return? How do we let the user know when something went
    //              wrong? Do we return a string, maybe an int, or maybe some custom return object?
    //              Do we catch the validation exceptions, or assume the user can catch those and deal with them
    //              how they wish?
    //          - How do we get input? Do we modify this method signature to take a string, or do we
    //              actually wait for IO in this method? I think waiting for IO makes the most sense, but
    //              we would have to properly document that so it doesn't surprise the user.
    public void parse(String input) throws ValidationException{
        // TODO: Full end-to-end parsing, calling lex and validate
//        try {
//            validate(lex(input));
//        } catch (ValidationException err) {
//            // TODO: Do something with it, tell the user somehow
//        }

        validate(lex(input)); //will be caught by programmer
    }

    /**
     * Lexing of the raw input into an array of tokens.
     *
     * @param rawArguments Raw arguments - that is, whatever the user input.
     * @return List of tokens, each with values, kinds, and potentially names (if non-positional: empty string if unnamed)
     */
    private ArrayList<ArgToken> lex(String rawArguments) {
        // TODO: Lex
        var lexer = new Lexer(rawArguments);
        var res = lexer.lex();
        System.out.println(res);
        return res;
    }


    /**
     * Validates the tokens with the arguments currently in the namespace. I.e., checks their types, ranges,
     *  number of args, etc. Throws a validation error if something is awry.
     * @param tokens List of tokens, as generated from lex.
     */
    private void validate(ArrayList<ArgToken> tokens) throws ValidationException {
        // TODO: Validate
        throw new NotImplementedException("Validator not implemented");
        //throw new ValidationException("TODO: more descriptive error message here");
    }
}
