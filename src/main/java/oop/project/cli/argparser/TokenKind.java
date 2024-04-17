package oop.project.cli.argparser;

/**
 * TokenKind enum, for use by ArgTokens.
 * Likely can be extended if we want to make the parser more robust.
 */
public enum TokenKind {
    POSITIONAL_ARG,
    NAMED_ARG
}
