package oop.project.cli.argparser;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Argument <T>{
    public final String[] names;
    public final String ref;
    public final Class<T> type;
    public final IRange<T> range;
    public final String nArgs;
    public final String helpMessage;
    public final String helpName;
    public final boolean required;
    public final boolean positional; // might not be necessary?

    ArrayList<T> value;
    public Argument(ArgumentBuilder<T> builder) {
        names = builder.names;
        ref = builder.ref;
        type = builder.type;
        range = builder.range;
        nArgs = builder.nArgs;
        helpMessage = builder.helpMessage;
        helpName = builder.helpName;
        required = builder.required;
        positional = builder.positional;

        value = new ArrayList<>();  // Default value is blank...might not be the best design
    }

    public ArrayList<T> getValue() {
        return value;
    }
}
