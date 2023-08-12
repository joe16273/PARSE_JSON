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
                    return -Integer.parseInt(numberValue);
                }
            }
            if (numberValue.contains(".")) {
                return Double.parseDouble(numberValue); // Return as decimal if it has a dot
            } else {
                return Integer.parseInt(numberValue); // Return as integer otherwise
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

    public static void main(String[] args) {
        String jsonString= "{" +
            "    \"level1\": {\n" +
            "        \"level2\": {\n" +
            "            \"level3\": {\n" +
            "                \"level4\": {\n" +
            "                    \"level5\": {\n" +
            "                        \"value\": \"deeply nested value\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        Map<String, Object> level1= (Map<String, Object>) jsonObject.get("level1");
        Map<String, Object> level2= (Map<String, Object>) level1.get("level2");
        Map<String, Object> level3= (Map<String, Object>) level2.get("level3");
        Map<String, Object> level4= (Map<String, Object>) level3.get("level4");
        Map<String, Object> level5= (Map<String, Object>) level4.get("level5");
        Object value= level5.get("value");
        System.out.println(value);

    }
}
