package oop.project.cli;

import oop.project.cli.argparser.*;

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
            case "multFlags" -> multFlags(arguments);
            case "multPos" -> multPos(arguments);
            case "all" -> all(arguments);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }


    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a

    //good place to test/showcase your functionality in context.

    static Map<String, List<Object>> string(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("string", "testing functionality of string parsing");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("Pass in any string to validate")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }

    static  Map<String, List<Object>> ints(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("int", "testing functionality of int parsing");
        parser.addArgument(new ArgumentBuilder<>(BigInteger.class, "positional")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("Pass in any integer to validate")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }

    static Map<String, List<Object>> decimal(String arguments)  throws ValidationException {
        ArgumentParser parser = new ArgumentParser("decimal", "testing functionality of decimal parsing");
        parser.addArgument(new ArgumentBuilder<>(BigInteger.class, "positional")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("Pass in any decimal to validate")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }

    static Map<String, List<Object>> date(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("date", "testing date parsing");
        parser.addArgument(new ArgumentBuilder<>(Date.class, "positional")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("*")
                .setHelpMessage("required date positional")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }


    //TODO POSSIBLE BOOLEAN FLIP FOR NO ARGUMENTS
    static Map<String, List<Object>> noArgs(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("noArgs", "testing functionality of flag with no arguments parsing");
        parser.addArgument(new ArgumentBuilder<>(String.class, "flag", "-f", "--flag")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("Pass in any decimal to validate")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("flag", res);
    }

    static Map<String, List<Object>> flagNArgsQuestion(String arguments)  throws ValidationException {
        ArgumentParser parser = new ArgumentParser("flagNArgsQuestion", "testing functionality of flag with ? args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "flag", "-f", "--flag")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("Pass in strings to validate if ? operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("flag", res);
    }


    static Map<String, List<Object>> flagNArgsPlus(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("flagNArgsPlus", "testing functionality of flag with + args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "flag", "-f", "--flag")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("+")
                .setHelpMessage("Pass in strings to validate if + operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("flag", res);
    }

    static Map<String, List<Object>> flagNArgsStar(String arguments)  throws ValidationException {
        ArgumentParser parser = new ArgumentParser("flagNArgsStar", "testing functionality of flag with * args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "flag", "-f", "--flag")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("*")
                .setHelpMessage("Pass in strings to validate if + operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("flag", res);
    }


    static Map<String, List<Object>> positionalNArgsQuestion(String arguments)   throws ValidationException {
        ArgumentParser parser = new ArgumentParser("positionalNArgQuestion", "testing functionality of positional with ? args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("Pass in strings to validate if ? operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }
    static Map<String, List<Object>> positionalNArgsPlus(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("positionalNArgQuestion", "testing functionality of positional with + args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("+")
                .setHelpMessage("Pass in strings to validate if + operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }

    static Map<String, List<Object>> positionalNArgsStar(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("positionalNArgsStar", "testing functionality of positional with * args");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("*")
                .setHelpMessage("Pass in strings to validate if * operation correct")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }
    static Map<String, List<Object>> requiredFalse(String arguments)  throws ValidationException {
        ArgumentParser parser = new ArgumentParser("requiredFalse", "testing functionality required false");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("optional string positional")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }
    static Map<String, List<Object>> requiredTrue(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("requiredTrue", "testing functionality required true");
        parser.addArgument(new ArgumentBuilder<>(String.class, "positional")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("*")
                .setHelpMessage("required string positional")
                .build());

        parser.parse(arguments); //if fails, will throw validateException
        List<Object> res = new ArrayList<Object>(parser.getArgument("positional").getValue());
        return Map.of("positional", res);
    }

    static Map<String, List<Object>> multFlags(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("multFlags", "testing functionality for multiple flags");
        parser.addArgument(new ArgumentBuilder<>(String.class, "flag1", "-f1", "--flag1")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("*")
                .setHelpMessage("required string flag")
                .build());

        parser.addArgument(new ArgumentBuilder<>(BigInteger.class, "flag2", "-f2", "--flag2")
                .setPositional(false)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("optional integer flag")
                .build());

        parser.parse(arguments); //if fails, will throw validateException

        List<Object> flag1 = new ArrayList<Object>(parser.getArgument("flag1").getValue());
        List<Object> flag2 = new ArrayList<Object>(parser.getArgument("flag2").getValue());
        return Map.of("flag1", flag1, "flag2", flag2);
    }

    static Map<String, List<Object>> multPos(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("multPos", "testing functionality for multiple positionals");
        parser.addArgument(new ArgumentBuilder<>(String.class, "source")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("optional dest positional")
                .build());

        parser.addArgument(new ArgumentBuilder<>(String.class, "dest")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("required dest positional")
                .build());

        parser.parse(arguments); //if fails, will throw validateException

        List<Object> source = new ArrayList<Object>(parser.getArgument("source").getValue());
        List<Object> dest = new ArrayList<Object>(parser.getArgument("dest").getValue());
        return Map.of("source", source, "dest", dest);
    }

    static Map<String, List<Object>> all(String arguments) throws ValidationException {
        ArgumentParser parser = new ArgumentParser("all", "testing functionality for multiple positionals");

        parser.addArgument(new ArgumentBuilder<>(String.class, "flag1", "-f1", "--flag1")
                .setPositional(false)
                .setRequired(false)
                .setNArgs("?")
                .setHelpMessage("optional string flag")
                .build());

        parser.addArgument(new ArgumentBuilder<>(String.class, "flag2", "-f2", "--flag2")
                .setPositional(false)
                .setRequired(false)
                .setNArgs("?")
                .setHelpMessage("optional string flag")
                .build());

        parser.addArgument(new ArgumentBuilder<>(String.class, "flag3", "-f3", "--flag3")
                .setPositional(false)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("required string flag")
                .build());

        parser.addArgument(new ArgumentBuilder<>(String.class, "source")
                .setPositional(true)
                .setRequired(false)
                .setNArgs("*")
                .setHelpMessage("optional dest positional")
                .build());

        parser.addArgument(new ArgumentBuilder<>(String.class, "dest")
                .setPositional(true)
                .setRequired(true)
                .setNArgs("?")
                .setHelpMessage("required dest positional")
                .build());

        parser.parse(arguments); //if fails, will throw validateException

        List<Object> flag1 = new ArrayList<Object>(parser.getArgument("flag1").getValue());
        List<Object> flag2 = new ArrayList<Object>(parser.getArgument("flag2").getValue());
        List<Object> flag3 = new ArrayList<Object>(parser.getArgument("flag3").getValue());
        List<Object> source = new ArrayList<Object>(parser.getArgument("source").getValue());
        List<Object> dest = new ArrayList<Object>(parser.getArgument("dest").getValue());
        return Map.of("flag1", flag1, "flag2", flag2, "flag3", flag3, "source", source, "dest", dest);
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: <your decimal type>} (optional)
     *     - If your project supports default arguments, you could also parse
     *       this as a non-optional decimal value using a default of 0.0.
     *  - {@code right: <your decimal type>} (required)
     */


}
