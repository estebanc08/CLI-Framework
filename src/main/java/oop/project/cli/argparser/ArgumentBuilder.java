package oop.project.cli.argparser;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ArgumentBuilder {
    protected String[] names;
    protected String ref;
    protected Class<?> type;  // note: we may need to change this
    protected IRange<Comparable<?>> range;  // also this too :(
    protected String nArgs;
    protected String helpMessage;
    protected String helpName;
    protected boolean required;
    protected boolean positional;

    private final String identifierPattern = "[a-zA-Z_][a-zA-Z0-9_-]*";

    /**
     * Validates a string to ensure that it conforms with the regex pattern we've chosen
     *  for identifiers.
     * @param s String to check
     * @return Whether the given string passed
     */
    private boolean validateIdentifierRegex(String s) {
        return s.matches(identifierPattern);
    }

    public ArgumentBuilder(Class<?> type, String ref, String... names) {
        Set<Class<?>> validTypes = new HashSet<>(Arrays.asList(
                BigInteger.class,
                BigDecimal.class,
                Date.class,
                String.class
        ));

        // If not a valid type
        if (!validTypes.contains(type))
            throw new ArgumentBuilderException("Error: "+type+" is not a valid type. " +
                    "Valid types: " + validTypes);

        // If ref does not match regex
        if (!validateIdentifierRegex(ref))
            throw new ArgumentBuilderException("Error: Ref does not conform to identifier regex " + identifierPattern);

        // If any of the names provided do not match the regex
        var invalidNames = Arrays.stream(names).filter((name) -> !validateIdentifierRegex(name)).toArray();
        if (invalidNames.length != 0) {
            throw new ArgumentBuilderException("Error: Given name " + Arrays.toString(invalidNames)
                    + "does not conform to identifier regex " + identifierPattern);
        }

        this.type = type;
        this.ref = ref;
        this.names = names;
    }


    // I honestly have no clue how we'll do type checking with IRange (or how java generics even work)
    public ArgumentBuilder setRange(IRange<Comparable<?>> range) {
        this.range = range;
        return this;
    }
    public ArgumentBuilder setNArgs(String nArgs) {
        String errorMessage = "Error: Invalid input to setNArgs. (Must be ?,*,+ or integer)";
        String nArgFlags = "?*+";

        // Check if the string is one of the flags we've set, and if so, we're done
        if (nArgs.length() == 1 && nArgFlags.contains(nArgs)) {
            this.nArgs = nArgs;
            return this;
        }

        // Validate that the string is an integer
        Scanner sc = new Scanner(nArgs.trim());
        if (!sc.hasNextInt())
            throw new ArgumentBuilderException(errorMessage);

        // Move forward
        sc.nextInt();

        // Verify that there is nothing left
        if (sc.hasNext())
            throw new ArgumentBuilderException(errorMessage);

        this.nArgs = nArgs;
        return this;
    }
    public ArgumentBuilder setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
        return this;
    }
    public ArgumentBuilder setHelpName(String helpName) {
        this.helpName = helpName;
        return this;
    }
    public ArgumentBuilder setRequired(boolean required) {
        this.required = required;
        return this;
    }
    public ArgumentBuilder setPositional(boolean positional) {
        this.positional = true;
        return this;
    }
    public Argument<?> build() {
        return new Argument<>(this);
    }
}