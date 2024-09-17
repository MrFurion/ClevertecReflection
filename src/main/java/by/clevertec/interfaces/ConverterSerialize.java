package by.clevertec.interfaces;

import org.json.JSONObject;

public interface ConverterSerialize {
    JSONObject serialize(Object obj) throws IllegalAccessException;
}
