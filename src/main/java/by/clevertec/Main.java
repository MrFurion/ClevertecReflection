package by.clevertec;

import by.clevertec.parsers.JsonParserToObject;
import by.clevertec.interfaces.JsonParser;
import by.clevertec.models.Customer;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            JsonParser parser = new JsonParserToObject();
            Customer customer = parser.convertJsonToObject(Customer.class);
            out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}