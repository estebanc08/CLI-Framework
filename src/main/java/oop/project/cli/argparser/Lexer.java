package oop.project.cli.argparser;

import com.google.common.io.CharStreams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final CharStream chars;
    Lexer(String input) {
        chars = new CharStream(input);
    }


    ArrayList<ArgToken> lex() {
        var tokens = new ArrayList<ArgToken>();
        while(chars.has(0)){
            while (chars.has(0) && match("[ ]")) {} //git rid of whitespace between words
            if(peek("[\\.0-9\"\\[]") || peek("[\\-]", "[\\.0-9]")){
                tokens.add(lexPositional());
            }else if(peek("\\-" , "[a-zA-Z]") || peek("[\\-]", "[\\-]", "[a-zA-Z]")){
                tokens.add(lexNamed());
            }
            if(chars.has(0) && !peek("[ ]"))
                throw new ParseException("Required space between flags or positional values");
        }
        return tokens;
    }

    private ArgToken lexFlag(){
        return null;
    };


    private String lexString(){
        StringBuilder curr = new StringBuilder();
        match("\"");
        while (chars.has(0) && !peek("\"")) { // Read until the last "
            if(peek("[\n\r]"))
                throw new ParseException("Illegal New line in string");
            curr.append(chars.get(0));
            chars.advance(1);
        }
        if(peek("\""))
            chars.advance(1); // Consume the last "
        else
            throw new ParseException("Need quotation at end of string");
        return curr.toString();
    }

    private BigDecimal lexNumber(){ //will store all numbers as decimals and later check if expecting int that number is valid int
        StringBuilder curr = new StringBuilder();
        if (peek("-", "[0-9]") || peek("-", "\\.", "[0-9]")) {
            curr.append(chars.get(0));
            chars.advance(1);
        }
        while(chars.has(0) && peek("[0-9\\.]")){
            curr.append(chars.get(0));
            chars.advance(1);
        }
        try{
            return new BigDecimal(curr.toString());
        }catch(Exception e){
            throw new ParseException("Invalid number value");
        }
    }

    private Object lexObject(){
        //TODO: Need to differentiate between dates and negative values. Should dates be passed as strings and then type coerced later?
        StringBuilder curr = new StringBuilder();
        if (peek("\"")) { // Check if string
            return lexString();
        } else if(peek("[0-9-\\.]")){
            return lexNumber();
        }
        return curr.toString();
    }

    private ArrayList<Object> lexList(){
        ArrayList<Object> list = new ArrayList<>();
        match("[\\[]");
        while(match("[ ]")) {}; //want to ignore whitespace
        while (!peek("[\\]]")) {
            list.add(lexObject());
            while(match("[ ]")) {};
        }
        match("[\\]]");
        return list;
    }
    private ArgToken lexPositional() {
        // positionalNArgsQuestion ["hi" "hello" 1 2 3 45]
        ArrayList<Object> vals = new ArrayList<>();
        if (peek("[\\[]")) { // lexing positional multiple values
            vals.addAll(lexList());
        }else{ // lexing string or number
            vals.add(lexObject());
        }
        System.out.println(vals);
        return new ArgToken(ArgToken.Type.POSITIONAL_ARG, vals);
    };

    private ArgToken lexNamed(){
        return null;
    };

    private boolean peek(Object... objects) {
        for (var i = 0; i < objects.length; i++) {
            if (!chars.has(i)  || !test(objects[i], chars.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean match(Object... objects) {
        var peek = peek(objects);
        if (peek) {
            chars.advance(objects.length);
        }
        return peek;
    }
    
     private static boolean test(Object object, char character) {
        return switch (object) {
            case Character c -> character == c;
            case String regex -> Character.toString(character).matches(regex);
            case List<?> options -> options.stream().anyMatch(o -> test(o, character));
            default -> throw new AssertionError(object);
        };
    }


    private static final class CharStream {

        private final String input;
        private int index = 0;
        private int length = 0;

        private CharStream(String input) {
            this.input = input;
        }


        public boolean has(int offset) {
            return index + length + offset < input.length();
        }

        public char get(int offset) {
            if (!has(offset)) {
                throw new IllegalArgumentException("Broken lexer invariant.");
            }
            return input.charAt(index + length + offset);
        }

        public void advance(int chars) {
            length += chars;
        }

        public ArgToken emit(ArgToken.Type type, ArrayList<Object> vals) {
            var token = new ArgToken(type, vals);
            index += length;
            length = 0;
            return token;
        }

    }


}
