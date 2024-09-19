package by.clevertec.parsers;

import by.clevertec.interfaces.JsonParser;
import by.clevertec.interfaces.ParserStringToMap;
import by.clevertec.interfaces.ReadJson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonParserToObject implements JsonParser {

    ParserStringToMap parserStringToMap = new ParserStringToMapImpl();

    @Override
    public <T> T convertJsonToObject(Class<T> clazz) throws Exception {
        ReadJson readJson = new ReadJsonImpl();
        String json = readJson.readJson();
        Map<String, Object> map = parseJson(json);
        return mapToObject(map, clazz);
    }

    public Map<String, Object> parseJson(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parserStringToMap.parseJsonObject(json);
        } else if (json.startsWith("[")) {
            return Collections.singletonMap("array", parserStringToMap.parseJsonArray(json));
        }
        throw new IllegalArgumentException("Invalid JSON format");
    }

    private <T> T mapToObject(Map<String, Object> map, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = map.get(field.getName());

            if (value != null) {
                if (isPrimitiveOrWrapper(field.getType())) {
                    field.set(instance, convertValue(value.toString(), field.getType()));
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
                    Class<?> elementType = (Class<?>) collectionType.getActualTypeArguments()[0];

                    if (value instanceof List) {
                        List<?> valueList = (List<?>) value;
                        List<Object> list = valueList.stream()
                                .map(v -> {
                                    try {
                                        if (v instanceof Map) {
                                            return mapToObject((Map<String, Object>) v, elementType);
                                        } else {
                                            return convertValue(v.toString(), elementType);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                })
                                .collect(Collectors.toList());
                        field.set(instance, list);
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    ParameterizedType mapType = (ParameterizedType) field.getGenericType();
                    Class<?> keyType = (Class<?>) mapType.getActualTypeArguments()[0];
                    Class<?> valueType = (Class<?>) mapType.getActualTypeArguments()[1];

                    if (value instanceof Map) {
                        Map<?, ?> valueMap = (Map<?, ?>) value;
                        Map<Object, Object> mapValue = valueMap.entrySet().stream()
                                .map(entry -> {
                                    try {
                                        Object key = convertValue(entry.getKey().toString(), keyType);
                                        Object mapValueItem = convertValue(entry.getValue().toString(), valueType);
                                        return new AbstractMap.SimpleEntry<>(key, mapValueItem);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                })
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        field.set(instance, mapValue);
                    }
                } else {
                    if (value instanceof Map) {
                        field.set(instance, mapToObject((Map<String, Object>) value, field.getType()));
                    }
                }
            }
        }
        return instance;
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Double.class ||
                clazz == Boolean.class ||
                clazz == UUID.class ||
                clazz == LocalDate.class ||
                clazz == OffsetDateTime.class;
    }

    private Object convertValue(String value, Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == UUID.class) {
            return UUID.fromString(value);
        } else if (type == LocalDate.class) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (type == OffsetDateTime.class) {
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        return value;
    }
}
