package by.clevertec.interfaces;

public interface ConverterDeserializer {
    <T> T fromJson(String jsonString, Class<T> clazz) throws Exception;
}
