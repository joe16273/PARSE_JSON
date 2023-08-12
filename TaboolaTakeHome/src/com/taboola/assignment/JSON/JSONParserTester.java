package com.taboola.assignment.JSON;

import java.util.List;
import java.util.Map;

public class JSONParserTester {
    public static void main(String[] args) {
        testValidJson();
        testEscapedCharacters();
        testJsonObject();
        testEdgeCases();
        testArrayValues();
        testInvalidJSON();
        testNumericValues();
        testEmptyObject();
        testEmptyInput();

        System.out.println("All tests passed successfully.");
    }

    public static void testValidJson() {
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
        assert value.equals("deeply nested value");
    }

    public static void testEscapedCharacters() {
        String jsonString= "{\"text\": \"This is a \\\"quoted\\\" text.\"}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        String text= (String) jsonObject.get("text");
        assert text.equals("This is a \"quoted\" text.");

    }

    public static void testJsonObject() {
        String jsonString= "{" +
            "\"debug\": \"on\"," +
            "\"window\": {" +
            "\"title\": \"sample\"," +
            "\"size\": 500" +
            "}" +
            "}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        String debug= (String) jsonObject.get("debug");
        Map<String, Object> window= (Map<String, Object>) jsonObject.get("window");
        String title= (String) window.get("title");
        int size= (int) window.get("size");

        // Expected outputs
        assert debug.equals("on");
        assert title.equals("sample");
        assert size == 500;

    }

    public static void testEdgeCases() {
        String jsonString= "{" +
            "\"emptyString\": \"\"," +
            "\"zero\": 0," +
            "\"negative\": -123," +
            "\"booleanTrue\": true," +
            "\"booleanFalse\": false," +
            "\"nullValue\": null" +
            "}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        String emptyString= (String) jsonObject.get("emptyString");

        int zero= (int) jsonObject.get("zero");
        int negative= (int) jsonObject.get("negative");
        boolean booleanTrue= (boolean) jsonObject.get("booleanTrue");
        boolean booleanFalse= (boolean) jsonObject.get("booleanFalse");
        Object nullValue= jsonObject.get("nullValue");

        // Expected outputs
        assert emptyString.equals("");
        assert zero == 0;
        assert negative == -123;
        assert booleanTrue;
        assert !booleanFalse;
        assert nullValue == null;

    }

    public static void testArrayValues() {
        String jsonString= "{\"numbers\": [1, 2.5, -3, \"text\", true, false, null]}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        List<Object> numbers= (List<Object>) jsonObject.get("numbers");

        // Expected outputs
        assert numbers.get(0).equals(1);
        assert numbers.get(1).equals(2.5);
        assert numbers.get(2).equals(-3);
        assert numbers.get(3).equals("text");
        assert numbers.get(4).equals(true);
        assert numbers.get(5).equals(false);
        assert numbers.get(6) == null;

    }

    public static void testInvalidJSON() {
        String invalidJsonString= "{\"key\": \"value\", \"missingValue\"}";

        try {
            JSONLexer lexer= new JSONLexer(invalidJsonString);
            List<Token> tokens= lexer.tokenize();

            JSONParser parser= new JSONParser(tokens);
            Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

            System.out.println("Invalid JSON test failed: The JSON should be invalid.");
        } catch (Exception e) {
            System.out.println("Invalid JSON test passed: " + e.getMessage());
        }
    }

    public static void testNumericValues() {
        String jsonString= "{" +
            "\"integer\": 123," +
            "\"negative\": -456," +
            "\"floating\": 12.34," +
            "\"negativeFloating\": -0.987," +
            "\"scientific\": 1.23e5," +
            "\"negativeScientific\": -4.56E-3" +
            "}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        int integer= (int) jsonObject.get("integer");
        int negative= (int) jsonObject.get("negative");
        double floating= (double) jsonObject.get("floating");
        double negativeFloating= (double) jsonObject.get("negativeFloating");
        double scientific= (double) jsonObject.get("scientific");
        double negativeScientific= (double) jsonObject.get("negativeScientific");

        assert integer == 123;
        assert negative == -456;
        assert Math.abs(floating - 12.34) < 1e-5;
        assert Math.abs(negativeFloating + 0.987) < 1e-5;
        assert Math.abs(scientific - 123000) < 1e-5;
        assert Math.abs(negativeScientific + 0.00456) < 1e-5;

    }

    public static void testEmptyObject() {
        String jsonString= "{\"emptyObject\": {}}";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);
        Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();

        Map<String, Object> emptyObject= (Map<String, Object>) jsonObject.get("emptyObject");

        // Validate emptyObject is empty
        assert emptyObject.isEmpty();
    }

    public static void testEmptyInput() {
        String jsonString= "";

        JSONLexer lexer= new JSONLexer(jsonString);
        List<Token> tokens= lexer.tokenize();

        JSONParser parser= new JSONParser(tokens);

        try {
            Map<String, Object> jsonObject= (Map<String, Object>) parser.parse();
            System.out
                .println("Empty input test failed: Expected exception, but parsed successfully.");
        } catch (Exception e) {
            System.out.println("Empty input test passed: " + e.getMessage());
        }
    }

}
