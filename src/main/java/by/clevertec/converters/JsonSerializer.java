package by.clevertec.converters;

import by.clevertec.interfaces.ConverterSerialize;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonSerializer implements ConverterSerialize {

    public String toJson(Object obj) throws IllegalAccessException {
        return serialize(obj).toString();
    }

    public JSONObject serialize(Object obj) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value == null) {
                jsonObject.put(field.getName(), JSONObject.NULL);
            } else if (isPrimitiveOrWrapper(value.getClass())) {
                jsonObject.put(field.getName(), value);
            } else if (value instanceof List<?>) {
                jsonObject.put(field.getName(), serializeList((List<?>) value));
            } else if (value instanceof Map<?, ?>) {
                jsonObject.put(field.getName(), serializeMap((Map<?, ?>) value));
            } else {
                jsonObject.put(field.getName(), serialize(value));
            }
        }
        return jsonObject;
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                clazz == UUID.class || Number.class.isAssignableFrom(clazz) ||
                clazz == Boolean.class || clazz == Character.class ||
                clazz == LocalDate.class || clazz == OffsetDateTime.class;
    }

    private List<Object> serializeList(List<?> list) throws IllegalAccessException {
        List<Object> jsonList = new ArrayList<>();
        for (Object item : list) {
            if (isPrimitiveOrWrapper(item.getClass())) {
                jsonList.add(item);
            } else {
                jsonList.add(serialize(item));
            }
        }
        return jsonList;
    }

    private Map<Object, Object> serializeMap(Map<?, ?> map) throws IllegalAccessException {
        Map<Object, Object> jsonMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (isPrimitiveOrWrapper(value.getClass())) {
                jsonMap.put(key.toString(), value);
            } else {
                jsonMap.put(key.toString(), serialize(value));
            }
        }
        return jsonMap;
    }
}
