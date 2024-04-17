package oop.project.cli;

import oop.project.cli.argparser.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ScenariosTests {

    @Nested
    class Add {

        @ParameterizedTest
        @MethodSource
        public void testAdd(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testAdd() {
            return Stream.of(
                Arguments.of("Add", "add 1 2", Map.of("left", 1, "right", 2)),
                Arguments.of("Missing Argument", "add 1", null),
                Arguments.of("Extraneous Argument", "add 1 2 3", null),
                Arguments.of("Not A Number", "add one two", null),
                Arguments.of("Not An Integer", "add 1.0 2.0", null)
            );
        }

    }

    @Nested
    class Div {

        @ParameterizedTest
        @MethodSource
        public void testSub(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSub() {
            return Stream.of(
                Arguments.of("Sub", "sub --left 1.0 --right 2.0", Map.of("left", 1.0, "right", 2.0)),
                Arguments.of("Left Only", "sub --left 1.0", null),
                Arguments.of("Right Only", "sub --right 2.0", Map.of("left", Optional.empty(), "right", 2.0)),
                Arguments.of("Missing Value", "sub --right", null),
                Arguments.of("Extraneous Argument", "sub --right 2.0 extraneous", null),
                Arguments.of("Misspelled Flag", "sub --write 2.0", null),
                Arguments.of("Not A Number", "sub --right two", null)
            );
        }

    }

    @Nested
    class Sqrt {

        @ParameterizedTest
        @MethodSource
        public void testSqrt(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSqrt() {
            return Stream.of(
                Arguments.of("Valid", "sqrt 4", Map.of("number", 4)),
                Arguments.of("Imperfect Square", "sqrt 3", Map.of("number", 3)),
                Arguments.of("Zero", "sqrt 0", Map.of("number", 0)),
                Arguments.of("Negative", "sqrt -1", null)
            );
        }

    }

    @Nested
    class Calc {

        @ParameterizedTest
        @MethodSource
        public void testCalc(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testCalc() {
            return Stream.of(
                Arguments.of("Add", "calc add", Map.of("subcommand", "add")),
                Arguments.of("Sub", "calc sub", Map.of("subcommand", "sub")),
                Arguments.of("Sqrt", "calc sqrt", Map.of("subcommand", "sqrt")),
                Arguments.of("Missing", "calc", null),
                Arguments.of("Invalid", "calc unknown", null)
            );
        }

    }

    @Nested
    class Date {

        @ParameterizedTest
        @MethodSource
        public void testDate(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testDate() {
            return Stream.of(
                Arguments.of("Date", "date 2024-01-01", Map.of("date", LocalDate.of(2024, 1, 1))),
                Arguments.of("Invalid", "date 20240401", null)
            );
        }

    }

    @Nested
    class ValidateTypes {

        @ParameterizedTest
        @MethodSource
        public void testStringTypes(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testStringTypes() {
            return Stream.of(
                    /** this is for `type positional` where positional required and is of type * */
                    Arguments.of("Standard String - no bracket", "string \"hi\"",  Map.of("positional", new ArrayList<>(List.of("hi")))),
                    Arguments.of("Standard String - bracket", "string [\"hi\" \"hello\"]",  Map.of("positional", new ArrayList<>(List.of("hi", "hello")))),
                    Arguments.of("Missing quotes", "string hi", null),
                    Arguments.of("Missing Quote", "string \"hi", null),
                    Arguments.of("Missing Quote - brackets", "string [\"hi]", null),
                    Arguments.of("Missing Bracket", "string [\"hi", null),
                    Arguments.of("Bracket in quote", "string \"[\"hi\"]\"", Map.of("positional",  new ArrayList<>(List.of("[\"hi\"]")))),
                    Arguments.of("No space separated string", "string [\"hi\"\"hello\"]", Map.of("positional",  new ArrayList<>(List.of("hi", "hello")))),
                    Arguments.of("Bracket in bracket", "string [\"]\"]", Map.of("positional",  new ArrayList<>(List.of("]")))),
                    Arguments.of("escape characters", "string \"h\t''\\\n\"", Map.of("positional",  new ArrayList<>(List.of("h\t''\\\n"))))
            );
        }


    }

    @Nested
    class ValidateNArgsFlags {
        /**
            ALL ARGUMENTS ARE ASSUMED TO BE STRING TYPES
         */

        @ParameterizedTest
        @MethodSource
        public void testNArgsNone(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsNone() {
            return Stream.of(
                    /**this is for validate --flag=[args?] */
                    Arguments.of("Missing arg without equals", "validate --flag",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "validate --flag=\"\"",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "validate --flag=[]",  Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "validate --flag=\"1\"", null),
                    Arguments.of("One arg with brackets", "validate --flag=[\"1\"]",  null),
                    Arguments.of("One arg with brackets multiple words", "validate --flag=[\"There are multiple words\"]", null),
                    Arguments.of("One arg with brackets multiple words", "validate --flag=[\"There are multiple words\" \"another one\"]",  null),
                    Arguments.of("More than one arg", "validate --flag=[\"1\" \"2\"]",  null)
            );
        }


        @ParameterizedTest
        @MethodSource
        public void testNArgsQuestion(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsQuestion() {
            return Stream.of(
                    /**this is for validate --flag=[args?] */
                    Arguments.of("Missing arg without equals", "validate --flag",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "validate --flag=\"\"",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "validate --flag=[]",  Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "validate --flag=\"1\"",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "validate --flag=[\"1\"]",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets multiple words", "validate --flag=[\"There are multiple words\"]",  Map.of("flag", new ArrayList<>(List.of("There are multiple words")))),
                    Arguments.of("One arg with brackets multiple words", "validate --flag=[\"There are multiple words\" \"another one\"]",  null),
                    Arguments.of("More than one arg", "validate --flag=[\"1\" \"2\"]",  null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsPlus(String name, String command, Object expected) {
            test(command, expected);
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsStar(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsStar() {
            return Stream.of(
                    /**this is for validate --flag=[args?] */
                    Arguments.of("Missing arg without equals", "validate --flag",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "validate --flag=\"\"",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "validate --flag=[]",  Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "validate --flag=\"1\"",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "validate --flag=[\"1\"]",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("More than one arg", "validate --flag=[\"1\" \"2\" \"3\" \"4\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2", "3", "4"))))
            );
        }

        public static Stream<Arguments> testNArgsPlus() {
            return Stream.of(
                    /**this is for validate --flag=[args+]*/
                    Arguments.of("Missing arg without equals", "validate --flag",  null),
                    Arguments.of("Missing arg with and quotations", "validate --flag=\"\"",  null),
                    Arguments.of("Missing arg with and brackets", "validate --flag=[]",  null),
                    Arguments.of("One arg without brackets", "validate --flag=\"1\"",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "validate --flag=[\"1\"]",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("More than one arg", "validate --flag=[\"1\" \"2\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2"))))
            );
        }

    }

    @Nested
    class ValidateNArgsPositional {
        /**
         ALL ARGUMENTS ARE ASSUMED TO BE STRING TYPES
         */

        @ParameterizedTest
        @MethodSource
        public void testNArgsQuestion(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsQuestion() {
            return Stream.of(
                    /**
                     this is for validate [positional?]
                     */
                    Arguments.of("Empty Positional", "validate",  Map.of("flag", Optional.empty())),
                    Arguments.of("Unquoted Flag", "validate --flag", null),
                    Arguments.of("Quoted Flag", "validate \"--flag\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Regular Number", "validate \"1\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Multiple Positional Args", "validate [\"1\" \"2\" \"hi\"]",  null),
                    Arguments.of("String Arguments unquoted", "validate [hi]",  null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsPlus(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsPlus() {
            return Stream.of(
                    /**
                     this is for validate [positional+]
                     */
                    Arguments.of("Empty Positional", "validate", null),
                    Arguments.of("Unquoted Flag", "validate --flag", null),
                    Arguments.of("Quoted Flag", "validate \"--flag\"", Map.of("positional", new ArrayList<>(List.of("--flag")))),
                    Arguments.of("Regular Number", "validate \"1\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Multiple Positional Args ", "validate [\"1\" \"2\" \"hi\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2", "hi"))))
            );
        }


        @ParameterizedTest
        @MethodSource
        public void testNArgsStar(String name, String command, Object expected) {
            test(command, expected);
        }
        public static Stream<Arguments> testNArgsStar() {
            return Stream.of(
                    /**
                        this is for validate [positional*]
                     */
                    Arguments.of("Empty Positional", "validate",  Map.of("flag", Optional.empty())),
                    Arguments.of("Unquoted Flag", "validate --flag", null),
                    Arguments.of("Quoted Flag", "validate \"--flag\"", Map.of("positional", new ArrayList<>(List.of("--flag")))),
                    Arguments.of("Multiple Positional Args", "validate [\"1\" \"2\" \"hi\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2", "hi"))))
                    );
        }
    }

    @Nested
    class ValidateHelp {
        @ParameterizedTest
        @MethodSource
        public void testHelpDefault(String name, String command, Object expected) {
            test(command, expected);
        }
        public static Stream<Arguments> testHelpDefault() {
            return Stream.of(
                    Arguments.of("Empty Positional", "helpDefault -h",  "")
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testHelpMessage(String name, String command, Object expected) {
            test(command, expected);
        }
        public static Stream<Arguments> testHelpMessage() {
            return Stream.of(
                    Arguments.of("Empty Positional", "helpCustom -h",  "Successful Help")
            );
        }
    }

    @Nested
    class ValidateRequired {
        @ParameterizedTest
        @MethodSource
        public void testRequiredFalse(String name, String command, Object expected) {
            test(command, expected);
        }
        public static Stream<Arguments> testRequiredFalse() {
            return Stream.of(
                    /**testing for required positional=false */
                    Arguments.of("Empty Positional", "requiredFalse",  Map.of("positional", Optional.empty())),
                    Arguments.of("Given Positional", "requiredFalse \"1\"",  Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Given Positional", "requiredFalse [\"1\" \"2\"]",  Map.of("positional", new ArrayList<>(List.of("1", "2"))))
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testRequiredTrue(String name, String command, Object expected) {
            test(command, expected);
        }
        public static Stream<Arguments> testRequiredTrue() {
            return Stream.of(
                    /**testing for required positional=false */
                    Arguments.of("Empty Positional", "requiredTrue", null),
                    Arguments.of("Given Positional", "requiredTrue [\"1\" \"2\"]",  Map.of("positional", new ArrayList<>(List.of("1", "2"))))
            );
        }
    }





        private static void test(String command, Object expected)  {
        if (expected != null) {
            try {
                var result = Scenarios.parse(command);
                Assertions.assertEquals(expected, result);
            }catch(Exception e){
                Assertions.fail("unexpected exception thrown");
            }
        } else {
            //TODO: Update with your specific Exception class or whatever other
            //error handling model you use to check for specific library issues.
            Assertions.assertThrows(ValidationException.class, () -> {
                Scenarios.parse(command);
            });
        }
    }

}
