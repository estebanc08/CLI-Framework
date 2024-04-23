package oop.project.cli.argparser;

/**
 * Checked exception for when something goes wrong with validation (i.e. the user did something wrong.)
 */
public class ArgParseException extends Exception {
    public ArgParseException(String err) { super("Validation error: " + err); }
}
