package oop.project.cli.argparser;

import java.lang.reflect.Type;
import java.util.Arrays;

public class ArgumentBuilder<T> {
    protected String[] names;
    protected String ref;
    protected Class<T> type;
    protected IRange<T> range;
    protected String nArgs;
    protected String helpMessage;
    protected String helpName;
    protected boolean required;
    protected boolean positional;

    public ArgumentBuilder(Class<T> type, String ref, String... names) {
        this.type = type;
        this.ref = ref;
        this.names = names;
    }

    // I honestly have no clue how we'll do type checking with IRange (or how java generics even work)
    public ArgumentBuilder<T> setRange(IRange range) { this.range = range; return this; }
    public ArgumentBuilder<T> setNArgs(String nArgs) { this.nArgs = nArgs; return this; }
    public ArgumentBuilder<T> setHelpMessage(String helpMessage) { this.helpMessage = helpMessage; return this; }
    public ArgumentBuilder<T> setHelpName(String helpName) { this.helpName = helpName; return this; }
    public ArgumentBuilder<T> setRequired(boolean required) { this.required = required; return this; }
    public ArgumentBuilder<T> setPositional(boolean positional) { this.positional = positional; return this; }
    public Argument<T> build() {
        return new Argument<>(this);
    }
}
