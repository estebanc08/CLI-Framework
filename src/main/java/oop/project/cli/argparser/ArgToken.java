package oop.project.cli.argparser;

import java.util.ArrayList;

/**
 * Argument token - record used for parsing.
 * @param type Type of the argument.
 * @param name Name of the argument - that is, whatever the input name was (example n="value" has name n)
 * @param value Value of the argument. Can be a list, for example in the case of <code>numbers=[1 2 3]</code>
 */
public record ArgToken(
        Type type,
        String name,
        ArrayList<Object> value
){
    public enum Type {
        POSITIONAL_ARG,
        NAMED_ARG,
        FLAG
    }

}
