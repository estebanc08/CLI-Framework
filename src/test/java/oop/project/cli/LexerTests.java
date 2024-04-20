package oop.project.cli;

import oop.project.cli.argparser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import java.util.Date;
public class LexerTests {
    /**
        Lexer is not passed in command name, just the arguments
     */
    @Nested
    class PositionalTests {
        @ParameterizedTest
        @MethodSource
        public void testStrings(String name, String command, ArrayList<ArgToken> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testStrings() {
            return Stream.of(
                    Arguments.of("Standard one word string", "\"string\"", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of("string")))))),
                    Arguments.of("Multi word string", "\"string with spaces\"", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of("string with spaces")))))),
                    Arguments.of("Nested single quote", "\"string1 'string2'\"", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of("string1 'string2'")))))),
                    Arguments.of("Nested double quotes", "\"string1 \\\"string2\\\"\"", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of("string1 \"string2\"")))))), //TODO FIX POSSIBLY
                    Arguments.of("Missing Quotes", "string", null),
                    Arguments.of("Missing first Quote", "string\"", null),
                    Arguments.of("Missing last Quote", "\"string", null),
                    Arguments.of("Missing last Quote - spaced out", "\"string1 string2", null)
            );
        }
        @ParameterizedTest
        @MethodSource
        public void testIntegers(String name, String command, ArrayList<ArgToken> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testIntegers() {
            return Stream.of(
                    Arguments.of("Standard Number", "1", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigInteger("1"))))))),
                    Arguments.of("Negative Number", "-1", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigInteger("-1"))))))),
                    Arguments.of("Large Number", "2147483648", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigInteger("2147483648"))))))),
                    Arguments.of("Large Number - Negative", "-2147483650", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigInteger("-2147483650"))))))),
                    Arguments.of("Leading 0's", "00000000002", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigInteger("2"))))))),
                    Arguments.of("Character in Number", "12a", null),
                    Arguments.of("Character in Number Nested", "1a1", null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testDecimals(String name, String command, ArrayList<ArgToken> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testDecimals() {
            return Stream.of(
                    Arguments.of("Standard Decimal", "1.1", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("1.1"))))))),
                    Arguments.of("Standard Decimal", "1.0", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("1.0"))))))),
                    Arguments.of("Leading 0s", "000000001.1", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("1.1"))))))),
                    Arguments.of("Negative Decimal", "-1.1", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("-1.1"))))))),
                    Arguments.of("Large Decimal", "2147483648.123456789", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("2147483648.123456789"))))))),
                    Arguments.of("Large Decimal - Negative", "-2147483650.123456789", new ArrayList<>(List.of(
                            new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", new ArrayList<>(List.of(new BigDecimal("-2147483650.123456789"))))))),
                    Arguments.of("Multiple decimals", "1.2.1", null),
                    Arguments.of("Character in Decimal", "12.a", null),
                    Arguments.of("Character in Decimal Nested", "1.a1", null)
            );
        }

    }

    @Nested
    class FlagTests{
        //TODO: ADD BASIC TEST CASES FOR FLAGS
    }

    @Nested
    class NamedArgTests{
        //TODO: ADD BASIC TEST CASES FOR ARGUMENTS
    }

    private static void test(String command, ArrayList<ArgToken> expected )  {
        if (expected != null) {
            try {
                Assertions.assertEquals(expected, new Lexer(command).lex());
            } catch (ParseException e) {
                Assertions.fail(e.getMessage());
            }
        } else {
            Assertions.assertThrows(ParseException.class, () -> {
                new Lexer(command).lex();
            });
        }
    }


}
