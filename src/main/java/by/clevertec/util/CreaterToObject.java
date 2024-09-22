package by.clevertec.util;

import by.clevertec.interfaces.ObjectGenerator;
import by.clevertec.models.Customer;
import by.clevertec.models.Order;
import by.clevertec.models.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreaterToObject implements ObjectGenerator {


    public Customer createCustomer() {

        return Customer.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .dateBirth(LocalDate.of(2003, 6, 23))
                .orders(List.of(Order.builder()
                        .id(UUID.randomUUID())
                        .products(List.of(Product.builder()
                                .id(UUID.randomUUID())
                                .name("Phone")
                                .price(77.88)
                                .prices(Map.of(UUID.randomUUID(), BigDecimal.valueOf(899.99)))
                                .build()))
                        .createDate(OffsetDateTime.of(2024, 9, 22, 21, 56, 31, 524044200, ZoneOffset.ofHours(3)))
                        .build()))
                .build();
    }
}
