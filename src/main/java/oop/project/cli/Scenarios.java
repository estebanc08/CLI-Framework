package oop.project.cli;

import oop.project.cli.argparser.ValidationException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

public class Scenarios {

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    public static Map<String, List<Object>> parse(String command) throws ValidationException {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        var split = command.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "string" -> string(arguments);
            case "ints" -> ints(arguments);
            case "decimal" -> decimal(arguments);
            case "date" -> date(arguments);
            case "noArgs" -> noArgs(arguments);
            case "flagNArgsQuestion" -> flagNArgsQuestion(arguments);
            case "flagNArgsPlus" -> flagNArgsPlus(arguments);
            case "flagNArgsStar" -> flagNArgsStar(arguments);
            case "positionalNArgsQuestion" -> positionalNArgsQuestion(arguments);
            case "positionalNArgsPlus" -> positionalNArgsPlus(arguments);
            case "positionalNArgsStar" -> positionalNArgsStar(arguments);
            case "requiredFalse" -> requiredFalse(arguments);
            case "requiredTrue" -> requiredTrue(arguments);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }


    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a

    //good place to test/showcase your functionality in context.

    static  Map<String, List<Object>> string(String arguments) throws ValidationException {
        return Map.of("positional", new ArrayList<>(List.of(new BigInteger(arguments))));
    }

    static  Map<String, List<Object>> ints(String arguments) throws ValidationException {
        return Map.of("positional", new ArrayList<>(List.of(new BigInteger(arguments))));
    }

    static Map<String, List<Object>> decimal(String arguments) {
        //TODO implement
        return null;
    }

    static Map<String, List<Object>> noArgs(String arguments) {
        //TODO implement
        return null;
    }

    static Map<String, List<Object>> flagNArgsQuestion(String arguments) {
        //Todo Implement
        return null;
    }


    static Map<String, List<Object>> flagNArgsPlus(String arguments) {
        //Todo Implement
        return null;
    }

    static Map<String, List<Object>> flagNArgsStar(String arguments) {
        //Todo Implement
        return null;
    }


    static Map<String, List<Object>> positionalNArgsQuestion(String arguments) {
        //Todo Implement
        return null;
    }


    static Map<String, List<Object>> positionalNArgsPlus(String arguments) {
        //Todo Implement
        return null;
    }

    static Map<String, List<Object>> positionalNArgsStar(String arguments) {
        //Todo Implement
        return null;
    }

    static Map<String, List<Object>> requiredFalse(String arguments) {
        //Todo Implement
        return null;
    }

    static Map<String, List<Object>> requiredTrue(String arguments) {
        //Todo Implement
        return null;
    }







    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, List<Object>> date(String arguments) {
        //TODO: Parse arguments and extract values.
        LocalDate date = LocalDate.EPOCH;
        return null;
//        return Map.of("date", date);
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: <your decimal type>} (optional)
     *     - If your project supports default arguments, you could also parse
     *       this as a non-optional decimal value using a default of 0.0.
     *  - {@code right: <your decimal type>} (required)
     */
//    static  Map<String, Object> validate(String arguments) throws ValidationException {
//        /**
//        *    function has following structure
//        *    validate [--flag=[args]] pattern
//        */
//        var args = arguments.split(" ");
//        if(args.length > 2){
//            throw new ValidationException("passing in too many arguments");
//        }
//        Map<String, Object> res = new HashMap<>();
//        if(args[0].matches("^--flag=")){
//            var flagArgs = args[0].replaceFirst("^--flag=", "");
//            res.put("flag", flagArgs.split("")),
//        }
//
//        return Map.of("flag", "flag", "pattern", "HI");
//    }


}
