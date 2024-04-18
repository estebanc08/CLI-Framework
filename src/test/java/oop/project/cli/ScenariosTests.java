package oop.project.cli;

import oop.project.cli.argparser.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import java.util.Date;

public class ScenariosTests {

    @Nested
    class Add {

        @ParameterizedTest
        @MethodSource
        public void testAdd(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testSub(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testSqrt(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testCalc(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testDate(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testStringTypes(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testStringTypes() {
            return Stream.of(
                    /** this is for `type positional` where positional required and is of type string* */
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

        @ParameterizedTest
        @MethodSource
        public void testIntType(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testIntType() {
            return Stream.of(
                    /** this is for `type positional` where positional required and is of type int* */
                    Arguments.of("Standard Int", "ints 1",  Map.of("positional", new ArrayList<>(List.of(new BigInteger("1"))))),
                    Arguments.of("Decimal Value", "ints 1.0",  null), //TODO: Could be possible to return just 1 and round all decimals to nearest Integer
                    Arguments.of("Int inside quotes", "ints \"1\"", null), //TODO: Possibly change if type coercion implemented
                    Arguments.of("All String", "ints hi", null),
                    Arguments.of("Number and String", "ints 12k5", null),
                    Arguments.of("Negative Numbers", "ints -1", Map.of("positional", new ArrayList<>(List.of(new BigInteger("-1"))))),
                    Arguments.of("Large Number", "ints 2147483648", Map.of("positional", new ArrayList<>(List.of(new BigInteger("2147483648"))))),
                    Arguments.of("Large Number - Negative", "ints -2147483649", Map.of("positional", new ArrayList<>(List.of(new BigInteger("-2147483649")))))
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testDecimalType(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testDecimalType() {
            return Stream.of(
                    /** this is for `type positional` where positional required and is of type decimal* */
                    Arguments.of("Standard Decimal", "decimal 1.1",  Map.of("positional", new ArrayList<>(List.of(new BigDecimal("1.1"))))),
                    Arguments.of("Decimal Value", "decimal 1.0",  Map.of("positional", new ArrayList<>(List.of(new BigDecimal("1.0"))))), //TODO: Could be possible to return just 1 and round all decimals to nearest Integer
                    Arguments.of("Decimal inside quotes", "decimal \"1.1\"", null), //TODO: Possibly change if type coercion implemented
                    Arguments.of("String", "decimal hi", null),
                    Arguments.of("Scientific Notation", "decimal 1.2e3",Map.of("positional", new ArrayList<>(List.of(new BigDecimal("1200"))))),
                    Arguments.of("Scientific Notation - decimal", "ints 1.2e-3",Map.of("positional", new ArrayList<>(List.of(new BigDecimal("0.0012"))))),
                    Arguments.of("Negative Decimal", "decimal -1.1", Map.of("positional", new ArrayList<>(List.of(new BigInteger("-1.1"))))),
                    Arguments.of("Large Decimal", "decimal 1234.1212121212121212121212121212", Map.of("positional", new ArrayList<>(List.of(new BigInteger("1234.1212121212121212121212121212"))))),
                    Arguments.of("Large Decimal - Negative", "decimal -1234.1212121212121212121212121212", Map.of("positional", new ArrayList<>(List.of(new BigInteger("-2147483649")))))
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testDateType(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testDateType() {
            return Stream.of(
                    /** this is for `type positional` where positional required and is of type date(YYYY-MM-DD)* */
                    Arguments.of("Standard Date", "date 2024-04-18",  Map.of("positional", new ArrayList<>(List.of(LocalDate.parse("2024-04-18"))))),
                    Arguments.of("Invalid Date", "date 02-02-2002", null), //TODO: Possibly add flag to signify how date should be passed
                    Arguments.of("date inside flag", "date \"2024-04-18\"", null), //TODO: Possibly change if type coercion implemented
                    Arguments.of("Invalid Date - extra dash start", "-2024-04-18", null),
                    Arguments.of("Invalid Date - extra dash middle", "2024-04--18", null),
                    Arguments.of("Invalid Date - Missing Numbers", "2024-4-18", null) //todo, should this work?
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
        public void testNArgsNone(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsNone() {
            return Stream.of(
                    /**this is for validate --flag=[args?] */
                    Arguments.of("Missing arg without equals", "noArgs --flag",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "noArgs --flag=\"\"",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "noArgs --flag=[]",  Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "noArgs --flag=\"1\"", null),
                    Arguments.of("One arg with brackets", "noArgs --flag=[\"1\"]",  null),
                    Arguments.of("One arg with brackets multiple words", "noArgs --flag=[\"There are multiple words\"]", null),
                    Arguments.of("One arg with brackets multiple words", "noArgs --flag=[\"There are multiple words\" \"another one\"]",  null),
                    Arguments.of("More than one arg", "noArgs --flag=[\"1\" \"2\"]",  null)
            );
        }


        @ParameterizedTest
        @MethodSource
        public void testNArgsQuestion(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsQuestion() {
            return Stream.of(
                    /**this is for flagNArgsQuestion --flag=[args?] */
                    Arguments.of("Missing arg without equals", "flagNArgsQuestion --flag",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "flagNArgsQuestion --flag=\"\"",  Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "flagNArgsQuestion --flag=[]",  Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "flagNArgsQuestion --flag=\"1\"",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "flagNArgsQuestion --flag=[\"1\"]",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets multiple words", "flagNArgsQuestion --flag=[\"There are multiple words\"]",  Map.of("flag", new ArrayList<>(List.of("There are multiple words")))),
                    Arguments.of("One arg with brackets multiple words", "flagNArgsQuestion --flag=[\"There are multiple words\" \"another one\"]",  null),
                    Arguments.of("More than one arg", "flagNArgsQuestion --flag=[\"1\" \"2\"]",  null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsPlus(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsPlus() {
            return Stream.of(
                    /**this is for validate --flag=[args+]*/
                    Arguments.of("Missing arg without equals", "flagNArgsPlus --flag",  null),
                    Arguments.of("Missing arg with and quotations", "flagNArgsPlus --flag=\"\"",  null),
                    Arguments.of("Missing arg with and brackets", "flagNArgsPlus --flag=[]",  null),
                    Arguments.of("One arg without brackets", "flagNArgsPlus --flag=\"1\"",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "flagNArgsPlus --flag=[\"1\"]",  Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("More than one arg", "flagNArgsPlus --flag=[\"1\" \"2\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2"))))
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsStar(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsStar() {
            return Stream.of(
                    /**this is for flagNArgsStar --flag=[args?] */
                    Arguments.of("Missing arg without equals", "flagNArgsStar --flag", Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and quotations", "flagNArgsStar --flag=\"\"", Map.of("flag", Optional.empty())),
                    Arguments.of("Missing arg with and brackets", "flagNArgsStar --flag=[]", Map.of("flag", Optional.empty())),
                    Arguments.of("One arg without brackets", "flagNArgsStar --flag=\"1\"", Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("One arg with brackets", "flagNArgsStar --flag=[\"1\"]", Map.of("flag", new ArrayList<>(List.of("1")))),
                    Arguments.of("More than one arg", "flagNArgsStar --flag=[\"1\" \"2\" \"3\" \"4\"]", Map.of("flag", new ArrayList<>(List.of("1", "2", "3", "4"))))
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
        public void testNArgsQuestion(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsQuestion() {
            return Stream.of(
                    /**
                     this is for positionalNArgQuestion [positional?]
                     */
                    Arguments.of("Empty Positional", "positionalNArgQuestion",  Map.of("flag", Optional.empty())),
                    Arguments.of("Unquoted Flag", "positionalNArgQuestion --flag", null),
                    Arguments.of("Quoted Flag", "positionalNArgQuestion \"--flag\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Regular Number", "positionalNArgQuestion \"1\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Multiple Positional Args", "positionalNArgQuestion [\"1\" \"2\" \"hi\"]",  null),
                    Arguments.of("String Arguments unquoted", "positionalNArgQuestion [hi]",  null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testNArgsPlus(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }

        public static Stream<Arguments> testNArgsPlus() {
            return Stream.of(
                    /**
                     this is for validate [positional+]
                     */
                    Arguments.of("Empty Positional", "positionalNArgPlus", null),
                    Arguments.of("Unquoted Flag", "positionalNArgPlus --flag", null),
                    Arguments.of("Quoted Flag", "positionalNArgPlus \"--flag\"", Map.of("positional", new ArrayList<>(List.of("--flag")))),
                    Arguments.of("Regular Number", "positionalNArgPlus \"1\"", Map.of("positional", new ArrayList<>(List.of("1")))),
                    Arguments.of("Multiple Positional Args ", "positionalNArgPlus [\"1\" \"2\" \"hi\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2", "hi"))))
            );
        }


        @ParameterizedTest
        @MethodSource
        public void testNArgsStar(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testNArgsStar() {
            return Stream.of(
                    /**
                        this is for validate [positional*]
                     */
                    Arguments.of("Empty Positional", "positionalNArgStar",  Map.of("flag", Optional.empty())),
                    Arguments.of("Unquoted Flag", "positionalNArgStar --flag", null),
                    Arguments.of("Quoted Flag", "positionalNArgStar \"--flag\"", Map.of("positional", new ArrayList<>(List.of("--flag")))),
                    Arguments.of("Multiple Positional Args", "positionalNArgStar [\"1\" \"2\" \"hi\"]",  Map.of("flag", new ArrayList<>(List.of("1", "2", "hi"))))
                    );
        }
    }

//    @Nested
//    class ValidateHelp {
//        @ParameterizedTest
//        @MethodSource
//        public void testHelpDefault(String name, String command, Map<String, List<Object>> expected ) {
//            test(command, expected);
//        }
//        public static Stream<Arguments> testHelpDefault() {
//            return Stream.of(
//                    Arguments.of("Empty Positional", "helpDefault -h",  "")
//            );
//        }
//
//        @ParameterizedTest
//        @MethodSource
//        public void testHelpMessage(String name, String command, Map<String, List<Object>> expected ) {
//            test(command, expected);
//        }
//        public static Stream<Arguments> testHelpMessage() {
//            return Stream.of(
//                    Arguments.of("Empty Positional", "helpCustom -h",  "Successful Help")
//            );
//        }
//    }

    @Nested
    class ValidateRequired {
        @ParameterizedTest
        @MethodSource
        public void testRequiredFalse(String name, String command, Map<String, List<Object>> expected ) {
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
        public void testRequiredTrue(String name, String command, Map<String, List<Object>> expected ) {
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

    @Nested
    class ValidateCombined {
        @ParameterizedTest
        @MethodSource
        public void testMultipleFlags(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testMultipleFlags() {
            return Stream.of(
                    /**testing for multFlags --flag1*.string.required --flag2*.int.optional */
                    Arguments.of("Both Flags given value", "multFlags --flag1=[\"hi\"] --flag2=2",
                            Map.of( "flag1", new ArrayList<>(List.of("hi")),
                                    "flag2", new ArrayList<>(List.of(new BigInteger("1"))))),
                    Arguments.of("Optional flag empty present, no value", "multFlags --flag1=[\"hi\"] --flag2",
                            Map.of( "flag1", new ArrayList<>(List.of("hi")),
                                    "flag2", Optional.empty())),
                    /*TODO: Decide if present flag with no values is optional empty or array list with no values
                    *  i.e figure out if empty is the same is not present  --flag2 === noFlag ?
                    *  Current assumption is they are the same. Might have to change if given star argument as still can be present but no value */
                    Arguments.of("Optional flag not present", "multFlags --flag1=[\"hi\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("hi")),
                                    "flag2", Optional.empty())),
                    Arguments.of("Required flag not present", "multFlags --flag2=1", null),
                    Arguments.of("No Flags present", "multFlags", null),
                    Arguments.of("Order swapped", "multFlags --flag2=[1 2 3] --flag1=[\"hi\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("hi")),
                                    "flag2", new ArrayList<>(List.of(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"))))),
                    Arguments.of("Duplicate flags", "multFlags --flag1=[\"hi\"] --flag1=[\"hello\"]", null)
                    //TODO: DECIDE IF LAST VALUE WINS OR THROW ERROR: I SAY THROW ERROR FOR PREDICTABILITY
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testMultiplePositionals(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testMultiplePositionals() {
            return Stream.of(
                    /**testing for multPos [source*.string.optional] [dest?.string.required] */
                    Arguments.of("Both positionals given value", "multPos [\"file1.txt\"] [\"output.txt\"]",
                            Map.of( "source", new ArrayList<>(List.of("file1.txt")),
                                    "dest", new ArrayList<>(List.of("output.txt")))),
                    Arguments.of("First positional multiple values", "multPos [\"file1.txt\" \"file2.txt\"] [\"output.txt\"]",
                            Map.of( "source", new ArrayList<>(List.of("file1.txt", "file2.txt")),
                                    "dest",  new ArrayList<>(List.of("output.txt")))),
                    Arguments.of("Second positional multiple values", "multPos [\"file1.txt\" \"file2.txt\"] [\"output.txt\" \"output2.txt\"]", null),
                    Arguments.of("Missing first positional", "multPos [\"output.txt\"]",
                            Map.of( "source", Optional.empty(),
                                    "dest",  new ArrayList<>(List.of("output.txt")))),
                    Arguments.of("Required positional too many args and first one missing", "multPos [\"output.txt\" \"output2\"]",null),
                    Arguments.of("Missing both positionals", "multPos", null),
                    Arguments.of("Extra Positional", "multPos [\"1\"] [\"2\"] [\"3\"]", null)

            );
        }

        @ParameterizedTest
        @MethodSource
        public void testBoth(String name, String command, Map<String, List<Object>> expected ) {
            test(command, expected);
        }
        public static Stream<Arguments> testBoth() {
            return Stream.of(
                    /**testing for rem --flag1?.string.optional --flag2?.string.optional --flag3?.string.required [source*.string.optional] [source*.string.required]*/
                    Arguments.of("All values present", "rem --flag1=[\"f1\"] --flag2=[\"f2\"] --flag3=[\"f3\"] [\"source\"] [\"dest\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("f1")),
                                    "flag2", new ArrayList<>(List.of("f2")),
                                    "flag3", new ArrayList<>(List.of("f3")),
                                    "source", new ArrayList<>(List.of("source")),
                                    "dest", new ArrayList<>(List.of("dest")))),
                    Arguments.of("All optional missing", "rem --flag3=[\"f3\"][\"dest\"]",
                                    "flag3", new ArrayList<>(List.of("f3")),
                                    "dest", new ArrayList<>(List.of("dest"))),
                    Arguments.of("Required Flag missing", "rem --flag1=[\"f1\"] --flag2=[\"f2\"]  [\"source\"] [\"dest\"]", null),
                    Arguments.of("Required positional missing", "rem --flag1=[\"f1\"] --flag2=[\"f2\"] --flag3=[\"f3\"] ", null),
                    Arguments.of("Ordered reversed", "rem [\"source\"] [\"dest\"] --flag1=[\"f1\"] --flag2=[\"f2\"] --flag3=[\"f3\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("f1")),
                                    "flag2", new ArrayList<>(List.of("f2")),
                                    "flag3", new ArrayList<>(List.of("f3")),
                                    "source", new ArrayList<>(List.of("source")),
                                    "dest", new ArrayList<>(List.of("dest")))),
                    //TODO: NOT SURE IF ORDER OF CODE ABOVE SHOULD MATTER i.e FLAGS FIRST THEN POSITIONAL OR IF CAN BE POSITIONAL THEN FLAG
                    Arguments.of("Flag sandwich", "rem [\"source\"] --flag1=[\"f1\"] --flag2=[\"f2\"] --flag3=[\"f3\"] [\"dest\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("f1")),
                                    "flag2", new ArrayList<>(List.of("f2")),
                                    "flag3", new ArrayList<>(List.of("f3")),
                                    "source", new ArrayList<>(List.of("source")),
                                    "dest", new ArrayList<>(List.of("dest")))),
                    //TODO: DECIDE IF THIS SHOULD EVEN BE ALLOWED
                    Arguments.of("Flag sandwich", "rem --flag1=[\"f1\"] --flag2=[\"f2\"] [\"source\"] --flag3=[\"f3\"] [\"dest\"]",
                            Map.of( "flag1", new ArrayList<>(List.of("f1")),
                                    "flag2", new ArrayList<>(List.of("f2")),
                                    "flag3", new ArrayList<>(List.of("f3")),
                                    "source", new ArrayList<>(List.of("source")),
                                    "dest", new ArrayList<>(List.of("dest")))),
                    //TODO: ALSO DECIDE IF THIS IS ALLOWED. IF LAST ONE ALLOWED, SO SHOULD THIS ONE. DECIDE IF ORDER MATTERS
                    Arguments.of("Empty command", "rem", null)
            );
        }

    }






        private static void test(String command, Map<String, List<Object>> expected )  {
        if (expected != null) {
            try {
                var result = Scenarios.parse(command);
                Assertions.assertEquals(expected.size(), result.size());
                for (Map.Entry<String, List<Object>> entry : expected.entrySet()) {
                    String key = entry.getKey();
                    List<Object> expectedValue = entry.getValue();

                    Assertions.assertTrue(result.containsKey(key));

                    List<Object> actualValue = result.get(key);
                    Assertions.assertEquals(expectedValue.size(), actualValue.size());

                    for (int i = 0; i < expectedValue.size(); i++) {
                        Assertions.assertEquals(expectedValue.get(i), actualValue.get(i));
                    }
                }
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
