package com.taboola.assignment.JSON;

import java.util.ArrayList;
import java.util.List;

enum TokenType {
    LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, COLON, COMMA, STRING, NUMBER, BOOLEAN,
    NULL
}

class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type= type;
        this.value= value;
    }

    @Override
    public String toString() {
        return "Token{" +
            "type=" + type +
            ", value='" + value + '\'' +
            '}';
    }
}

class JSONLexer {
    private String input;
    private int position;
    private static final String JSON_WHITESPACE= " \t\b\n\r";
    private static final String JSON_ESCAPES= "\"\\/bfnrt";

    public JSONLexer(String input) {
        this.input= input;
        position= 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens= new ArrayList<>();
        while (position < input.length()) {
            char currentChar= input.charAt(position);
            switch (currentChar) {
            case '{':
                tokens.add(new Token(TokenType.LEFT_BRACE, "{"));
                position++ ;
                break;
            case '}':
                tokens.add(new Token(TokenType.RIGHT_BRACE, "}"));
                position++ ;
                break;
            case '[':
                tokens.add(new Token(TokenType.LEFT_BRACKET, "["));
                position++ ;
                break;
            case ']':
                tokens.add(new Token(TokenType.RIGHT_BRACKET, "]"));
                position++ ;
                break;
            case ':':
                tokens.add(new Token(TokenType.COLON, ":"));
                position++ ;
                break;
            case ',':
                tokens.add(new Token(TokenType.COMMA, ","));
                position++ ;
                break;
            case '"':
                position++ ;
                StringBuilder stringValue= new StringBuilder();
                while (position < input.length()) {
                    char charAtPosition= input.charAt(position);
                    if (charAtPosition == '\\') {
                        position++ ; // Consume the backslash
                        char escapedChar= input.charAt(position);
                        if (JSON_ESCAPES.indexOf(escapedChar) != -1) {
                            int escapeIndex= JSON_ESCAPES.indexOf(escapedChar);
                            stringValue.append('\\').append(JSON_ESCAPES.charAt(escapeIndex));
                        } else if (escapedChar == 'u') {
                            position++ ; // Consume 'u'
                            StringBuilder unicode= new StringBuilder();
                            for (int i= 0; i < 4; i++ ) {
                                unicode.append(input.charAt(position++ ));
                            }
                            stringValue.append("\\u").append(unicode.toString());
                        } else {
                            // Invalid escape sequence, treat as a literal character
                            stringValue.append('\\').append(escapedChar);
                        }
                        position++ ; // Consume the escaped character
                    } else if (charAtPosition == '"') {
                        position++ ; // Consume the closing double quote
                        tokens.add(new Token(TokenType.STRING, stringValue.toString()));
                        break;
                    } else {
                        stringValue.append(charAtPosition);
                        position++ ;
                    }
                }
                break;
            default:
                if (Character.isDigit(currentChar) || currentChar == '-') {
                    StringBuilder numberValue= new StringBuilder();
                    if (currentChar == '-') {
                        numberValue.append('-');
                        position++ ;
                    }
                    while (position < input.length() &&
                        (Character.isDigit(input.charAt(position)) ||
                            input.charAt(position) == '.')) {
                        numberValue.append(input.charAt(position));
                        position++ ;
                    }
                    tokens.add(new Token(TokenType.NUMBER, numberValue.toString()));
                } else if (Character.isLetter(currentChar)) {
                    StringBuilder wordValue= new StringBuilder();
                    while (position < input.length() &&
                        (Character.isLetterOrDigit(input.charAt(position)) ||
                            input.charAt(position) == '_')) {
                        wordValue.append(input.charAt(position));
                        position++ ;
                    }
                    String word= wordValue.toString();
                    if ("true".equals(word) || "false".equals(word)) {
                        tokens.add(new Token(TokenType.BOOLEAN, word));
                    } else if ("null".equals(word)) {
                        tokens.add(new Token(TokenType.NULL, word));
                    } else {
                        throw new RuntimeException("Invalid token: " + word);
                    }
                } else if (Character.isWhitespace(currentChar)) {
                    position++ ;
                } else {
                    throw new RuntimeException("Invalid character: " + currentChar);
                }
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        String jsonString= "\"Hello, \\\"world\\\"! This is a \\nnewline.\"";
        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
