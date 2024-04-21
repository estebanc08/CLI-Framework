package oop.project.cli.argparser;

import com.google.common.io.CharStreams;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lexer {

    private final CharStream chars;
    public Lexer(String input) {
        chars = new CharStream(input);
    }


    public ArrayList<ArgToken> lex() {
        var tokens = new ArrayList<ArgToken>();
        while(chars.has(0)){
            while (chars.has(0) && match("[ ]")) {} //git rid of whitespace between words
            if(peek("[\\.0-9\"\\[]") || peek("[\\-]", "[\\.0-9]")){
                tokens.add(new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", lexPositional()));
            }else if(peek("\\-" , "[a-zA-Z]") || peek("[\\-]", "[\\-]", "[a-zA-Z]")){
                tokens.addAll(lexNamed());
            }else{
                throw new ParseException("Not a valid positional value, named argument, or flag");
            }
            if(chars.has(0) && !peek("[ ]"))
                throw new ParseException("Required space between flags or positional values");
        }
        return tokens;
    }

    private String lexString() {
        StringBuilder curr = new StringBuilder();
        match("\"");
        while (chars.has(0)) {
            char currentChar = chars.get(0);
            if (currentChar == '\\') { // Handle escape characters
                if (chars.has(1)) {
                    char nextChar = chars.get(1);
                    if (nextChar == '"' || nextChar == '\\' || nextChar == 't' || nextChar == 'n') {
                        if (nextChar == 't') {
                            curr.append('\t');
                        } else if (nextChar == 'n') {
                            curr.append('\n');
                        } else {
                            curr.append(nextChar);
                        }
                        chars.advance(2);
                    } else {
                        throw new ParseException("Invalid escape sequence: \\" + nextChar);
                    }
                } else {
                    throw new ParseException("Invalid escape character at end of input");
                }
            } else if (currentChar == '"') { // Handle nested quotes
                if (chars.has(1) && chars.get(1) == '"') {
                    curr.append("\"");
                    chars.advance(2);
                } else {
                    break;
                }
            } else {
                curr.append(currentChar);
                chars.advance(1);
            }
        }
        if (peek("\"")) {
            chars.advance(1);
        } else {
            throw new ParseException("Missing closing quotation mark");
        }
        return curr.toString();
    }




    private Object lexNumber(){ //will store all numbers as decimals and later check if expecting int that number is valid int
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
            var res = new BigDecimal(curr.toString());
            if(res.scale() <= 0)
                return res.toBigInteger();
            else return res;
        }catch(Exception e){
            throw new ParseException("Invalid number value");
        }
    }

    private Object lexObject(){
        //TODO: Need to differentiate between dates and negative values. Should dates be passed as strings and then type coerced later?
        if (peek("\"")) { // Check if string
            return lexString();
        } else if(peek("[0-9-\\.]")){
            return lexNumber();
        }else{
            throw new ParseException("Unsupported type or invalid input for value");
        }
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
    private ArrayList<Object> lexPositional() {
        ArrayList<Object> vals = new ArrayList<>();
        if (peek("[\\[]")) { // lexing positional multiple values
            vals.addAll(lexList());
        }else{ // lexing string or number
            vals.add(lexObject());
        }
        return vals;
        /** return new ArgToken(ArgToken.Type.POSITIONAL_ARG, vals);
        / By return vals instead of argtoken, can reuse structure for lexNamed that does --flag=[], --flag=1 or --flag="word"*/
    };

    private ArrayList<ArgToken> lexNamed(){
        ArrayList<ArgToken> tokens = new ArrayList<ArgToken>();
        ArrayList<Object> vals = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        if(peek("[\\-]", "[\\-]", "[a-zA-Z]")){ // --flag
            chars.advance(2);
            name.append("--");
            while(chars.has(0) && peek("[a-zA-Z0-9_]")){
                name.append(chars.get(0));
                chars.advance(1);
            }
            if(match("=")){
                vals.addAll(lexPositional());
                tokens.add(new ArgToken(ArgToken.Type.NAMED_ARG, name.toString(), vals));
                return tokens;
            }else{
                tokens.add(new ArgToken(ArgToken.Type.FLAG, name.toString(), vals));
                return tokens;
            }
        }else { //flag with one -
            chars.advance(1);
            while(chars.has(0) && peek("[a-zA-Z]")) {
                tokens.add(new ArgToken(ArgToken.Type.FLAG, "-" + chars.get(0), vals));
                chars.advance(1);
            }
            return tokens;
        }
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

        public ArgToken emit(ArgToken.Type type, String name, ArrayList<Object> vals) {
            var token = new ArgToken(type, name, vals);
            index += length;
            length = 0;
            return token;
        }

    }
}
