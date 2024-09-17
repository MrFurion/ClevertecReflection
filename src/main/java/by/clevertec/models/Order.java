package by.clevertec.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private List<Product> products;
    private OffsetDateTime createDate;

    public Order(UUID id, List<Product> products, OffsetDateTime createDate) {
        this.id = id;
        this.products = products;
        this.createDate = createDate;
    }

    public Order() {
    }
}
