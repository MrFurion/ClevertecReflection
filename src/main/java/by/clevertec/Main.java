package by.clevertec;

import by.clevertec.converters.JsonDeserializer;
import by.clevertec.converters.JsonSerializer;
import by.clevertec.models.Customer;
import by.clevertec.models.Order;
import by.clevertec.models.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Product product = new Product(UUID.randomUUID(), "Product A", 99.99, Map.of(UUID.randomUUID(), BigDecimal.valueOf(199.99)));
            Order order = new Order(UUID.randomUUID(), List.of(product), OffsetDateTime.now());
            Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", LocalDate.of(1990, 5, 25), List.of(order));

            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.toJson(customer);
            System.out.println("++++++++++>" + json);

            JsonDeserializer deserializer = new JsonDeserializer();
            Customer deserializedCustomer = deserializer.fromJson(json, Customer.class);
            System.out.println("--------->" + deserializedCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}