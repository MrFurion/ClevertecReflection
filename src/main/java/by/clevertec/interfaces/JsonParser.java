package by.clevertec.interfaces;

public interface JsonParser {
    <T> T convertJsonToObject(Class<T> clazz) throws Exception;
}
