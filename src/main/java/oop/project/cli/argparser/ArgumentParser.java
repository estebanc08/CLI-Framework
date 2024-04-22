package oop.project.cli.argparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentParser {
    private final String programName;
    public String description;
    private final MappedData namespace = new MappedData();
    ArrayList<Argument<?>> arguments = new ArrayList<>();

    public ArgumentParser(String programName, String description) {
        this.programName = programName;
        this.description = description;
        this.namespace.map = new HashMap<>();
        var helpFlag = new ArgumentBuilder<>(String.class, "help", "-h", "--help")
                .setPositional(false)
                .setRequired(false)
                .setNArgs("0")
                .setHelpMessage("Help")
                .build();
        addArgument(helpFlag);
        this.namespace.map.put(helpFlag.ref, helpFlag);
    }

    /**
     * Adds an argument to the parser. This will add it to the namespace, but will not be given a value
     *  until parsing actually happens.
     *
     * @param argument Argument to add to the parser.
     */
    public void addArgument(Argument<?> argument) {
        // If any overlap between this argument's names and other, previously defined arguments, throw error
        var namespaceNames = arguments.stream().flatMap(arg -> Arrays.stream(arg.names)).toList();
        if (Arrays.stream(argument.names).anyMatch(namespaceNames::contains))
            { throw new ArgumentException("Name identifier already exists with one of the following names: "+ Arrays.toString(argument.names)); }

        // If any overlap between this argument's refs and other, throw error
        var namespaceRefs = arguments.stream().map(arg -> arg.ref).toList();
        if (namespaceRefs.contains(argument.ref))
            { throw new ArgumentException("Reference already exists: "+ argument.ref); }

        arguments.add(argument);
        namespace.map.put(argument.ref, argument);
    }

    /**
     * Fetches argument from the namespace, or {@code null} if none exists.
     *
     * @param ref String referencing an argument in the namespace
     * @return Argument associated with the string, or {@code null} if none exists.
     */
    public Argument<?> getArgument(String ref) {
        return namespace.map.get(ref);
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
    public MappedData parse(String input) throws ValidationException{
        // TODO: Full end-to-end parsing, calling lex and validate
//        try {
//            validate(lex(input));
//        } catch (ValidationException err) {
//            // TODO: Do something with it, tell the user somehow
//        }
        validate(new Lexer(input).lex(), namespace); //will be caught by programmer
        return namespace;
    }


    /**
     * Validates the tokens with the arguments currently in the namespace. I.e., checks their types, ranges,
     *  number of args, etc. Throws a validation error if something is awry.
     * @param tokens List of tokens, as generated from lex.
     */
    private void validate(ArrayList<ArgToken> tokens, MappedData data) throws ValidationException {
        var v = new Validator(arguments);
        v.validate(tokens);
    }

    public void invokeHelp() {
        System.out.print("Usage: " );
        StringBuilder positionals = new StringBuilder();
        int optional = 0;
        for(var entry : namespace.map.entrySet()){
            var value = entry.getValue();
            if(value.positional){
                var name = value.helpName == null ? value.ref : value.helpName;
                positionals.append("[").append(name).append(": ").append(value.type.getSimpleName()).append("] ");
            }else{
                if(!value.required){
                    optional++;
                    continue;
                }
                var name = value.helpName == null ? value.names[0] : value.helpName;
                System.out.print("<" + name +": "+value.type.getSimpleName()+ "> ");
            }
        }
        if(optional > 0) System.out.print("< options > ");
        System.out.println(positionals + "\nOptions:");

        for(var entry : namespace.map.entrySet()) {
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
