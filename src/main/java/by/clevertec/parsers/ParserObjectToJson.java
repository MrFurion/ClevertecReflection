package by.clevertec.parsers;

import by.clevertec.converters.ConvertSpecialTypes;
import by.clevertec.interfaces.Checker;
import by.clevertec.interfaces.ConverterBigDecimal;
import by.clevertec.interfaces.ObjectParser;
import by.clevertec.util.CheckerData;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserObjectToJson implements ObjectParser {
    ConverterBigDecimal cBigDecimal = new ConvertSpecialTypes();

    public String convertObjectToJson(Object obj) throws IllegalAccessException {
        StringBuilder json = new StringBuilder();
        Checker checker = new CheckerData();

        if (obj == null) {
            return "null";
        }

        Class<?> clazz = obj.getClass();

        if (checker.isPrimitiveOrWrapper(clazz)) {
            json.append("\"").append(obj).append("\"");
        } else if (obj instanceof BigDecimal) {
            json.append(obj);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            json.append(convertCollectionToJson((Collection<?>) obj));
        } else if (Map.class.isAssignableFrom(clazz)) {
            json.append(convertMapToJson((Map<?, ?>) obj));
        } else {
            json.append("{");
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                Object value = field.get(obj);
                json.append("\"").append(field.getName()).append("\":");

                if (value != null) {
                    String specialType = cBigDecimal.specialTypes(value);
                    if (specialType != null) {
                        json.append("\"").append(specialType).append("\"");
                    } else if (checker.isPrimitiveOrWrapper(value.getClass())) {
                        json.append("\"").append(value).append("\"");
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        json.append(convertCollectionToJson((Collection<?>) value));
                    } else if (Map.class.isAssignableFrom(field.getType())) {
                        json.append(convertMapToJson((Map<?, ?>) value));
                    } else {
                        json.append(convertObjectToJson(value));
                    }
                } else {
                    json.append("null");
                }

                if (i < fields.length - 1) {
                    json.append(",");
                }
            }

            json.append("}");
        }

        return json.toString();
    }

    private String convertCollectionToJson(Collection<?> collection) throws IllegalAccessException {
        StringBuilder json = new StringBuilder();
        json.append("[");
        json.append(collection.stream()
                .map(element -> {
                    try {
                        return convertObjectToJson(element);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return "null";
                    }
                })
                .collect(Collectors.joining(",")));
        json.append("]");
        return json.toString();
    }

    private String convertMapToJson(Map<?, ?> map) throws IllegalAccessException {
        StringBuilder json = new StringBuilder();
        json.append("{");
        String mapEntries = map.entrySet().stream()
                .map(entry -> {
                    try {
                        return "\"" + entry.getKey().toString() + "\":" + convertObjectToJson(entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return "null";
                    }
                })
                .collect(Collectors.joining(","));
        json.append(mapEntries);
        json.append("}");
        return json.toString();
    }
}
