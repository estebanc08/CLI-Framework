package oop.project.cli.argparser;

import java.util.ArrayList;

/**
 * Argument token - record used for parsing.
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
