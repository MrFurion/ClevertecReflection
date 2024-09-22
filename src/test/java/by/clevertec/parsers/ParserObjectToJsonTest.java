package by.clevertec.parsers;

import by.clevertec.interfaces.ObjectGenerator;
import by.clevertec.interfaces.ObjectParser;
import by.clevertec.models.Customer;
import by.clevertec.util.CreaterToObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserObjectToJsonTest {


    @Test
    void convertObjectToJson() throws JsonProcessingException, IllegalAccessException {
        //given
        ObjectGenerator objectGenerator = new CreaterToObject();
        Customer customer = objectGenerator.createCustomer();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);

        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        String jsonResult = objectMapper.writeValueAsString(customer);

        //when
        ObjectParser objectParser = new ParserObjectToJson();
        String expectedJson = objectParser.convertObjectToJson(customer);
        String formattedJsonResult = jsonResult.replaceAll("\\.\\d+", "");
        String formattedExpectedJson = expectedJson.replaceAll("\\.\\d+", "");


        //then
        assertEquals(formattedExpectedJson,formattedJsonResult);
    }
}