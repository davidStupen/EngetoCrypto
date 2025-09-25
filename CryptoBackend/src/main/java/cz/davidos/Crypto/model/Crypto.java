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

    
}
