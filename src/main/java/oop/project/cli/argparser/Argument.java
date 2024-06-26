package oop.project.cli.argparser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Argument <T extends Comparable<T>>{
    public final String[] names;
    public final String ref;
    public final Class<T> type;
    public final IRange<T> range;
    public final String nArgs;
    public final String helpMessage;
    public final String helpName;
    public final boolean required;
    public final boolean positional;
    public ArrayList<Object> value;

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
        value = new ArrayList<>();
    }

    public ArrayList<Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\nArgument{\n" +
                "\tnames=" + Arrays.toString(names) + '\n' +
                "\tref='" + ref + "'\n" +
                "\ttype=" + type + "\n" +
                "\trange=" + range + "\n" +
                "\tnArgs='" + nArgs  + "'\n" +
                "\thelpMessage='" + helpMessage + "'\n" +
                "\thelpName='" + helpName  +"'\n" +
                "\trequired=" + required + "\n" +
                "\tpositional=" + positional + "\n" +
                "}\n";
    }

}
