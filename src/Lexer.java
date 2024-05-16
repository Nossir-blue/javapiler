package compiler;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final String[] keywords = { "if", "else", "while", "return", "int", "float" };
    private static final String operators = "+-*/=><";
    private static final String separators = "(){},;";
    private final String input;
    private int pos = 0;
    private final int length;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
    }

    private char peek() {
        return pos < length ? input.charAt(pos) : '\0';
    }

    private char next() {
        return pos < length ? input.charAt(pos++) : '\0';
    }

    private boolean isKeyword(String word) {
        for (String keyword : keywords) {
            if (keyword.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < length) {
            char current = peek();
            if (Character.isWhitespace(current)) {
                next();
            } else if (Character.isLetter(current)) {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetterOrDigit(peek())) {
                    sb.append(next());
                }
                String word = sb.toString();
                if (isKeyword(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, word));
                }
            } else if (Character.isDigit(current)) {
                StringBuilder sb = new StringBuilder();
                while (Character.isDigit(peek())) {
                    sb.append(next());
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
            } else if (operators.indexOf(current) != -1) {
                tokens.add(new Token(TokenType.OPERATOR, Character.toString(next())));
            } else if (separators.indexOf(current) != -1) {
                tokens.add(new Token(TokenType.SEPARATOR, Character.toString(next())));
            } else {
                throw new RuntimeException("Unexpected character: " + current);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
