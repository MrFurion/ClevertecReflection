package by.clevertec.parsers;

import by.clevertec.helper.ReadJsonImpl;
import by.clevertec.interfaces.JsonParser;
import by.clevertec.interfaces.ReadJson;
import by.clevertec.models.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserJsonToObjectTest {

    @Test
    void convertJsonToObject() throws Exception {
        // given
        ReadJson readJson = new ReadJsonImpl();
        String json = readJson.readJson();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        Customer customerResult = objectMapper.readValue(json, Customer.class);

        // when
        JsonParser jsonParser = new ParserJsonToObject();
        Customer customerExpected = jsonParser.convertJsonToObject(Customer.class);

        // then
        assertEquals(customerExpected.toString(), customerResult.toString());
    }
}