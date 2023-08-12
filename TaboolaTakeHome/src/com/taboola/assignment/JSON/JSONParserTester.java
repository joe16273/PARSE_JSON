package com.taboola.assignment.JSON;

import java.util.List;
import java.util.Map;

public class JSONParserTester {
    public static void main(String[] args) {

        testComplexObject();

        System.out.println("All tests passed successfully!");
    }

    private static void testComplexObject() {
        String jsonString= "{" +
            "\"debug\": \"on\"," +
            "\"window\": {" +
            "   \"title\": \"sample\"," +
            "   \"size\": 500" +
            "}" +
            "}";

        test(jsonString, "{\"debug\":\"on\",\"window\":{\"title\":\"sample\",\"size\":500}}");
    }

    private static void test(String jsonString, String expected) {
        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        String jsonStringParsed= jsonObject.toString();

        assert jsonStringParsed.equals(expected) : "Test failed. Expected: " + expected +
            ", but got: " + jsonStringParsed;
        System.out.println("Test passed for JSON: " + jsonString);
    }
}
