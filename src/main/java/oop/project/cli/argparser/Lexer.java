package oop.project.cli.argparser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public final class Lexer {

    private final CharStream chars;
    String input;
    public Lexer(String input) {
        chars = new CharStream(input);
        this.input = input;
    }

    private int getSizeOfName(){
        String[] parts = input.split("=", 2);
        String key = parts[0];
        return key.length() - 1;
    }


    public ArrayList<ArgToken> lex() throws ArgParseException {
        var tokens = new ArrayList<ArgToken>();
        while(chars.hasNext()){
            while (chars.hasNext() && match("[ ]")) {} // get rid of whitespace between words
             if (peek(0,"\"") || peek(0,"[\\.0-9\\[]") || peek(0,"[\\-]", "[\\.0-9]")) {
                tokens.add(new ArgToken(ArgToken.Type.POSITIONAL_ARG, "positional", lexPositional()));
            } else if ((peek(getSizeOfName(),"[a-zA-Z0-9]", "=") && !peek(0, "[0-9]")) || peek(0,"\\-", "[a-zA-Z]") || peek(0,"[\\-]", "[\\-]", "[a-zA-Z]")) {
                tokens.addAll(lexNamed());
            } else {
                throw new ArgParseException("Not a valid positional value, named argument, or flag. Input: " + input + " " + getSizeOfName());
            }
            if(chars.hasNext() && !peek(0,"[ ]"))
                throw new ArgParseException("Required space between flags or positional values. Input: " + input);
        }
        return tokens;
    }




    private String lexString() throws ArgParseException {
        StringBuilder curr = new StringBuilder();
        match("\"");
        while (chars.hasNext()) {
            char currentChar = chars.getNext();
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
                        throw new ArgParseException("Invalid escape sequence: \\" + nextChar);
                    }
                } else {
                    throw new ArgParseException("Invalid escape character at end of input");
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
        if (peek(0,"\"")) {
            chars.advance(1);
        } else {
            throw new ArgParseException("Missing closing quotation mark");
        }
        return curr.toString();
    }




    private Object lexNumber() throws ArgParseException { //will store all numbers as decimals and later check if expecting int that number is valid int
        StringBuilder curr = new StringBuilder();
        if (peek(0,"-", "[0-9]") || peek(0,"-", "\\.", "[0-9]")) {
            curr.append(chars.getNext());
            chars.advance(1);
        }
        while(chars.hasNext() && peek(0,"[0-9\\.]")){
            curr.append(chars.getNext());
            chars.advance(1);
        }
        try{
            var res = new BigDecimal(curr.toString());
            if(res.scale() <= 0)
                return res.toBigInteger();
            else return res;
        }catch(Exception e){
            throw new ArgParseException("Invalid number value");
        }
    }

    private Object lexObject() throws ArgParseException {
        //TODO: Need to differentiate between dates and negative values. Should dates be passed as strings and then type coerced later?
        if (peek(0,"\"")) { // Check if string
            return lexString();
        } else if(peek(0,"[0-9-\\.]")){
            return lexNumber();
        }else{
            throw new ParseException("Unsupported type or invalid input for value");
        }
    }

    private ArrayList<Object> lexList() throws ArgParseException {
        ArrayList<Object> list = new ArrayList<>();
        match("[\\[]");
        while(match("[ ]")) {}; //want to ignore whitespace
        while (!peek(0,"[\\]]")) {
            list.add(lexObject());
            while(match("[ ]")) {};
        }
        match("[\\]]");
        return list;
    }
    private ArrayList<Object> lexPositional() throws ArgParseException {
        ArrayList<Object> vals = new ArrayList<>();
        if (peek(0,"[\\[]")) { // lexing positional multiple values
            vals.addAll(lexList());
        }else{ // lexing string or number
            vals.add(lexObject());
        }
        return vals;
        /** return new ArgToken(ArgToken.Type.POSITIONAL_ARG, vals);
        / By return vals instead of argtoken, can reuse structure for lexNamed that does --flag=[], --flag=1 or --flag="word"*/
    };

    private ArrayList<ArgToken> lexNamed() throws ArgParseException {
        ArrayList<ArgToken> tokens = new ArrayList<>();
        ArrayList<Object> vals = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        if(peek(0,"[\\-]", "[\\-]", "[a-zA-Z]")){ // --flag
            chars.advance(2);
            name.append("--");
            while(chars.hasNext() && peek(0,"[a-zA-Z0-9_]")){
                name.append(chars.getNext());
                chars.advance(1);
            }
            if (match("=")){
                vals.addAll(lexPositional());
                tokens.add(new ArgToken(ArgToken.Type.NAMED_ARG, name.toString(), vals));
            }
            else {
                tokens.add(new ArgToken(ArgToken.Type.FLAG, name.toString(), vals));
            }
            return tokens;
        }else if(peek(0, "[\\-]", "[a-zA-Z]")) { //flag with one -
            chars.advance(1);
            while (chars.hasNext() && peek(0, "[a-zA-Z]")) {
                tokens.add(new ArgToken(ArgToken.Type.FLAG, "-" + chars.get(0), vals));
                chars.advance(1);
            }
        }
        else {
            while (chars.has(0) && peek(0, "[a-zA-Z0-9_]")) {
                name.append(chars.get(0));
                chars.advance(1);
            }
            if (match("=")) {
                vals.addAll(lexPositional());
                tokens.add(new ArgToken(ArgToken.Type.NAMED_ARG, name.toString(), vals));
                return tokens;
            }
        }
            return tokens;
    };

    /** Given a list of regexes / characters, will peek forward that list's length and test each character against each regex.**/
    private boolean peek(int offset, Object... objects) {
        for (var i = 0; i < objects.length; i++) {
            if (!chars.has(offset + i) || !test(objects[i], chars.get(offset + i))) {
                return false;
            }
        }
        return true;
    }

    private boolean match(Object... objects) {
        var peek = peek(0, objects);
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


        /** See if the current input string has <code>offset</code> more characters left to lex */
        public boolean has(int offset) {
            return index + length + offset < input.length();
        }

        /** For readability's sake: just calls <code>has(0)</code> */
        private boolean hasNext() {
            return has(0);
        }

        /** Retrieves the character of our current position plus <code>offset</code> */
        public char get(int offset) {
            if (!has(offset)) {
                throw new IllegalArgumentException("Broken lexer invariant.");
            }
            return input.charAt(index + length + offset);
        }

        /** For readability's sake: just calls <code>get(0)</code> */
        private char getNext() {
            return get(0);
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
