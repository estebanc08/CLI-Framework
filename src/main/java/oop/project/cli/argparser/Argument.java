package oop.project.cli.argparser;

import java.lang.reflect.Type;

public class Argument {
    public final String[] names;
    public final String ref;
    public final Object type;
    public final IRange<Type> range;
    public final String nArgs;
    public final String helpMessage;
    public final String helpName;
    public final boolean required;
    public final boolean positional; // might not be necessary?
    public String value;

    public Argument(ArgumentBuilder builder) {
        names = builder.names;
        ref = builder.ref;
        type = builder.type;
        range = builder.range;
        nArgs = builder.nArgs;
        helpMessage = builder.helpMessage;
        helpName = builder.helpName;
        required = builder.required;
        positional = builder.positional;

        value = "";  // Default value is blank...might not be the best design
    }

    public String getValue() {
        return value;
    }
}
