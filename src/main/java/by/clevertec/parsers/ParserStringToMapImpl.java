package by.clevertec.parsers;

import by.clevertec.interfaces.ParserStringToMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserStringToMapImpl implements ParserStringToMap {


    public Map<String, Object> parseJsonObject(String json) {
        json = json.substring(1, json.length() - 1).trim();
        Map<String, Object> map = new LinkedHashMap<>();
        String[] keyValuePairs = splitJson(json);

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":", 2);
            String key = entry[0].trim().replaceAll("\"", "");
            String value = entry[1].trim();
            map.put(key, parseValue(value));
        }
        return map;
    }

    public List<Object> parseJsonArray(String json) {
        json = json.substring(1, json.length() - 1).trim();
        List<Object> list = new ArrayList<>();
        String[] values = splitJson(json);

        for (int i = 0; i < values.length; i++) {
            list.add(parseValue(values[i].trim()));
        }
        return list;
    }

    private Object parseValue(String value) {
        value = value.trim();
        if (value.startsWith("{")) {
            return parseJsonObject(value);
        } else if (value.startsWith("[")) {
            return parseJsonArray(value);
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.matches("-?\\d+")) {
            return Integer.parseInt(value);
        } else if (value.matches("-?\\d+\\.\\d+")) {
            return Double.parseDouble(value);
        } else if (value.matches("\".*\"")) {
            return value.replaceAll("^\"|\"$", ""); // Убираем кавычки
        }
        return value;
    }

    private String[] splitJson(String json) {
        List<String> result = new ArrayList<>();
        int braceCount = 0;
        int bracketCount = 0;
        int startIndex = 0;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') braceCount++;
            if (c == '}') braceCount--;
            if (c == '[') bracketCount++;
            if (c == ']') bracketCount--;
            if (c == ',' && braceCount == 0 && bracketCount == 0) {
                result.add(json.substring(startIndex, i).trim());
                startIndex = i + 1;
            }
        }
        result.add(json.substring(startIndex).trim());
        return result.toArray(new String[0]);
    }
}
