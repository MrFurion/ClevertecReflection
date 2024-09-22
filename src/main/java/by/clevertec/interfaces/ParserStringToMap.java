package by.clevertec.interfaces;

import java.util.List;
import java.util.Map;

public interface ParserStringToMap {

    Map<String, Object> parseJsonObject(String json);

    List<Object> parseJsonArray(String json);
}
