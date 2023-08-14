package com.taboola.assignment.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JSONParser {
    private List<Token> tokens;
    private int position;

    public JSONParser(List<Token> tokens) {
        this.tokens= tokens;
        position= 0;
    }

    /** Parses a JSON object.
     *
     * @return The parsed JSON object as a Map. */

    public Map<String, Object> parseObject() {
        Map<String, Object> jsonObject= new HashMap<>();
        consumeToken(TokenType.LEFT_BRACE);

        while (tokens.get(position).type != TokenType.RIGHT_BRACE) {
            String jsonKey= consumeToken(TokenType.STRING).value;
            consumeToken(TokenType.COLON);

            int initialPosition= position; // Store the initial position

            Object jsonValue= parse();

            // Check if parse() method modified the position, and if not, manually update it
            if (position == initialPosition) {
                position++ ;
            }

            jsonObject.put(jsonKey, jsonValue);

            if (tokens.get(position).type == TokenType.COMMA) {
                consumeToken(TokenType.COMMA);
            }
        }

        consumeToken(TokenType.RIGHT_BRACE);
        return jsonObject;
    }

    /** Parses a JSON array.
     *
     * @return The parsed JSON array as a List. */

    public List<Object> parseArray() {
        List<Object> jsonArray= new ArrayList<>();
        consumeToken(TokenType.LEFT_BRACKET);

        while (tokens.get(position).type != TokenType.RIGHT_BRACKET) {
            Object jsonValue= parse();
            jsonArray.add(jsonValue);

            if (tokens.get(position).type == TokenType.COMMA) {
                consumeToken(TokenType.COMMA);
            }
        }

        consumeToken(TokenType.RIGHT_BRACKET);
        return jsonArray;
    }

    /** Parses various types of JSON values.
     *
     * @return The parsed JSON value. */

    public Object parse() {
        Token currentToken= tokens.get(position);

        switch (currentToken.type) {
        case STRING:
            position++ ;
            return currentToken.value;
        case NUMBER:
            position++ ;
            String numberValue= currentToken.value;
            if (numberValue.startsWith("-")) {
                numberValue= numberValue.substring(1); // Remove the '-'
                if (numberValue.contains(".")) {
                    return -Double.parseDouble(numberValue);
                } else {
                    try {
                        return -Integer.parseInt(numberValue);
                    } catch (NumberFormatException e) {
                        return -Long.parseLong(numberValue);
                    }
                }
            } else {

                if (numberValue.contains(".")) {
                    return Double.parseDouble(numberValue); // Return as decimal if it has a dot
                } else {
                    try {
                        return Integer.parseInt(numberValue);
                    } catch (NumberFormatException e) {
                        return Long.parseLong(numberValue);
                    }

                }
            }
        case BOOLEAN:
            position++ ;
            return Boolean.parseBoolean(currentToken.value);
        case NULL:
            position++ ;
            return null;
        case LEFT_BRACE:
            return parseObject();
        case LEFT_BRACKET:
            return parseArray();
        default:
            throw new RuntimeException("Unexpected token type: " + currentToken.type);
        }
    }

    /** Consumes and validates the next token's type.
     *
     * @param expectedType The expected type of the next token.
     * @return The consumed token.
     * @throws RuntimeException if the actual token type doesn't match the expected type. */

    private Token consumeToken(TokenType expectedType) {
        Token currentToken= tokens.get(position);
        if (currentToken.type == expectedType) {
            position++ ;
            return currentToken;
        } else {
            throw new RuntimeException(
                "Expected token type: " + expectedType + ", but found: " + currentToken.type);
        }
    }

}
