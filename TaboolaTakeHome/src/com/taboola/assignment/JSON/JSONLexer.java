package com.taboola.assignment.JSON;
import java.util.ArrayList;
import java.util.List;

// Enum representing different types of JSON tokens
enum TokenType {
    LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, COLON, COMMA, STRING, NUMBER, BOOLEAN,
    NULL
}

//Class representing a token with type and value
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

//Lexer class responsible for tokenizing input JSON string
class JSONLexer {
    private String input;
    private int position;
    private static final String JSON_ESCAPES= "\"\\/bfnrt";

    public JSONLexer(String input) {
        this.input= input;
        position= 0;
    }

    // Tokenizes the input JSON string and returns a list of tokens
    public List<Token> tokenize() {
        List<Token> tokens= new ArrayList<>();
        while (position < input.length()) {
            char currentChar= input.charAt(position);
            // Use a switch statement to handle different characters
            switch (currentChar) {
            case '{':
                tokens.add(new Token(TokenType.LEFT_BRACE, "{"));
                position++ ;
                // Add a LEFT_BRACE token to the list and move the position
                break;
            case '}':
                tokens.add(new Token(TokenType.RIGHT_BRACE, "}"));
                position++ ;
                // Add a RIGHT_BRACE token to the list and move the position
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
                // code for parsing strings with escape characters
                position++ ;
                StringBuilder stringValue= new StringBuilder();
                while (position < input.length()) {
                    char charAtPosition= input.charAt(position);
                    if (charAtPosition == '\\') {
                        position++ ; // Consume the backslash
                        char escapedChar= input.charAt(position);
                        if (JSON_ESCAPES.indexOf(escapedChar) != -1) {
                            int escapeIndex= JSON_ESCAPES.indexOf(escapedChar);
                            stringValue.append(JSON_ESCAPES.charAt(escapeIndex));
                        } else if (escapedChar == 'u') {
                            position++ ; // Consume 'u'
                            StringBuilder unicode= new StringBuilder();
                            for (int i= 0; i < 4; i++ ) {
                                unicode.append(input.charAt(position++ ));
                            }
                            String unicodeValue= unicode.toString();
                            char unicodeChar= (char) Integer.parseInt(unicodeValue, 16);
                            stringValue.append(unicodeChar);

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
                    // (code for parsing numbers)
                    StringBuilder numberValue= new StringBuilder();
                    boolean isFloatingPoint= false;

                    if (currentChar == '-') {
                        numberValue.append('-');
                        position++ ;
                    }

                    while (position < input.length()) {
                        char charAtPosition= input.charAt(position);
                        if (Character.isDigit(charAtPosition)) {
                            numberValue.append(charAtPosition);
                        } else if (charAtPosition == '.' && !isFloatingPoint) {
                            numberValue.append(charAtPosition);
                            isFloatingPoint= true;
                        } else if ((charAtPosition == 'e' || charAtPosition == 'E') &&
                            isFloatingPoint) {
                                numberValue.append(charAtPosition);
                                position++ ;
                                if (position < input.length() && (input.charAt(position) == '+' ||
                                    input.charAt(position) == '-')) {
                                    numberValue.append(input.charAt(position));
                                    position++ ;
                                }
                                while (position < input.length() &&
                                    Character.isDigit(input.charAt(position))) {
                                    numberValue.append(input.charAt(position));
                                    position++ ;
                                }
                                break; // Exit the loop after handling scientific notation
                            } else {
                                break; // Exit the loop for any other character
                            }
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
                    // Throw an exception for invalid characters
                }
            }
        }
        return tokens;
    }

}
