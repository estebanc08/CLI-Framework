package oop.project.cli.argparser;

/**
 * Argument token - record used for parsing.
 */
public record ArgToken() {
    static TokenKind kind;
    static String name;
    static String value;
}