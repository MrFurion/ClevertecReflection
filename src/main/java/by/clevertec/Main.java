package by.clevertec;

import by.clevertec.interfaces.ObjectGenerator;
import by.clevertec.interfaces.ObjectParser;
import by.clevertec.interfaces.WriterJson;
import by.clevertec.parsers.ParserObjectToJson;
import by.clevertec.helper.WriterJsonImpl;
import by.clevertec.util.CreaterToObject;
import by.clevertec.parsers.ParserJsonToObject;
import by.clevertec.interfaces.JsonParser;
import by.clevertec.models.Customer;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            JsonParser parser = new ParserJsonToObject();
            Customer customer = parser.convertJsonToObject(Customer.class);
            out.println(customer);

            ObjectGenerator objectGenerator = new CreaterToObject();
            Customer newCustomer = objectGenerator.createCustomer();
            ObjectParser objectParser = new ParserObjectToJson();

            WriterJson writerJson = new WriterJsonImpl();
            String nameFileForJson = "newCustomer.json";
            writerJson.writerJson(objectParser.convertObjectToJson(newCustomer), nameFileForJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}