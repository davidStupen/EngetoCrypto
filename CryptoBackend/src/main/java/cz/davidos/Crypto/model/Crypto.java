package cz.davidos.Crypto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Crypto {
    private UUID id;
    private String name;
    private String symbol;
    private BigDecimal price;
    private double quantity;

    public Crypto(String name, String symbol, double quantity) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.symbol = symbol;
        this.price = ;
        this.quantity = quantity;
    }
}
