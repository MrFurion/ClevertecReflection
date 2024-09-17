package by.clevertec.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String name;
    private Double price;
    private Map<UUID, BigDecimal> prices;

    public Product(UUID id, String name, Double price, Map<UUID, BigDecimal> prices) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.prices = prices;
    }

    public Product() {
    }
}
