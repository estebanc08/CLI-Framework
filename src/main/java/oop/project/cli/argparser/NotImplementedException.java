package oop.project.cli.argparser;

/**
 * Unchecked exception for when we haven't implemented a method or some kind of functionality yet.
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String err) {
        super(err);
    }
}
