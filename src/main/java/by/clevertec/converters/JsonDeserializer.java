package by.clevertec.converters;

import by.clevertec.interfaces.ConverterDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonDeserializer implements ConverterDeserializer {
    public <T> T fromJson(String jsonString, Class<T> clazz) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        return deserialize(jsonObject, clazz);
    }

    private <T> T deserialize(JSONObject jsonObject, Class<T> clazz) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            if (jsonObject.has(fieldName)) {
                Object jsonValue = jsonObject.get(fieldName);

                if (jsonValue == JSONObject.NULL) {
                    field.set(obj, null);
                } else if (isPrimitiveOrWrapper(fieldType)) {
                    // Обработка UUID
                    if (fieldType == UUID.class && jsonValue instanceof String) {
                        field.set(obj, UUID.fromString((String) jsonValue));
                    }
                    // Обработка LocalDate
                    else if (fieldType == LocalDate.class && jsonValue instanceof String) {
                        field.set(obj, LocalDate.parse((String) jsonValue));
                    }
                    // Обработка OffsetDateTime
                    else if (fieldType == OffsetDateTime.class && jsonValue instanceof String) {
                        field.set(obj, OffsetDateTime.parse((String) jsonValue));
                    }
                    // Обработка Double
                    else if (fieldType == Double.class && jsonValue instanceof BigDecimal) {
                        field.set(obj, ((BigDecimal) jsonValue).doubleValue());
                    }
                    // Обработка всех других примитивных типов и оберток
                    else {
                        field.set(obj, jsonValue);
                    }
                } else if (List.class.isAssignableFrom(fieldType)) {
                    field.set(obj, deserializeList(jsonObject.getJSONArray(fieldName), field));
                } else if (Map.class.isAssignableFrom(fieldType)) {
                    field.set(obj, deserializeMap(jsonObject.getJSONObject(fieldName), field));
                } else {
                    field.set(obj, deserialize((JSONObject) jsonValue, fieldType));
                }
            }
        }
        return obj;
    }

    private List<Object> deserializeList(JSONArray jsonArray, Field field) throws Exception {
        List<Object> list = new ArrayList<>();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> listItemType = (Class<?>) genericType.getActualTypeArguments()[0];

        for (int i = 0; i < jsonArray.length(); i++) {
            if (isPrimitiveOrWrapper(listItemType)) {
                list.add(jsonArray.get(i));
            } else {
                list.add(deserialize(jsonArray.getJSONObject(i), listItemType));
            }
        }
        return list;
    }

    private Map<Object, Object> deserializeMap(JSONObject jsonObject, Field field) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> keyType = (Class<?>) genericType.getActualTypeArguments()[0];
        Class<?> valueType = (Class<?>) genericType.getActualTypeArguments()[1];

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            Object mapKey = keyType == UUID.class ? UUID.fromString(key) : key;

            if (isPrimitiveOrWrapper(valueType)) {
                map.put(mapKey, value);
            } else {
                map.put(mapKey, deserialize((JSONObject) value, valueType));
            }
        }
        return map;
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                clazz == UUID.class || clazz == LocalDate.class ||
                clazz == OffsetDateTime.class || clazz == BigDecimal.class ||
                clazz == Double.class || clazz == Float.class ||
                clazz == Long.class || clazz == Integer.class ||
                clazz == Short.class || clazz == Byte.class ||
                clazz == Boolean.class || clazz == Character.class;
    }
}
