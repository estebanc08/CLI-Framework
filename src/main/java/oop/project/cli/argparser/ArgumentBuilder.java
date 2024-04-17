package oop.project.cli.argparser;

import java.lang.reflect.Type;

public class ArgumentBuilder {
    protected String[] names;
    protected String ref;
    protected Type type;  // note: we may need to change this
    protected IRange<Type> range;  // also this too :(
    protected String nArgs;
    protected String helpMessage;
    protected String helpName;
    protected boolean required;
    protected boolean positional;

    ArgumentBuilder(Type type, String ref, String... names) {
        this.type = type;
        this.ref = ref;
        this.names = names;
    }

    // I honestly have no clue how we'll do type checking with IRange (or how java generics even work)
    ArgumentBuilder setRange(IRange range) { this.range = range; return this; }
    ArgumentBuilder setNArgs(String nArgs) { this.nArgs = nArgs; return this; }
    ArgumentBuilder setHelpMessage(String helpMessage) { this.helpMessage = helpMessage; return this; }
    ArgumentBuilder setHelpName(String helpName) { this.helpName = helpName; return this; }
    ArgumentBuilder setRequired(boolean required) { this.required = required; return this; }
    ArgumentBuilder setPositional(boolean positional) { this.positional = positional; return this; }
    Argument build() {
        return new Argument(this);
    }
}
