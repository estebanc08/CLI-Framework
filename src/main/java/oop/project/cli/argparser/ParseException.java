package oop.project.cli.argparser;

public final class ParseException extends RuntimeException {
    public ParseException(String message) {
        super("Parse error:" + message);
    }
}