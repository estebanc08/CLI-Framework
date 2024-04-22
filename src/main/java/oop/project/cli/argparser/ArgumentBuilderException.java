package oop.project.cli.argparser;

public class ArgumentBuilderException extends RuntimeException {
    public ArgumentBuilderException(String err) { super("Argument Builder error:"+err); }
}