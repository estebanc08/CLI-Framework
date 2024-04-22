package oop.project.cli.argparser;

public class ArgumentException extends RuntimeException {
    public ArgumentException(String err) { super("Argument error: " + err); }
}
