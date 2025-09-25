package cz.davidos.Crypto.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TotalValueDTO {
    private String err;
    private BigDecimal totalValue;

    public TotalValueDTO(String err, BigDecimal totalValue) {
        this.err = err;
        this.totalValue = totalValue;
    }

    public TotalValueDTO(String err) {
        this(err, BigDecimal.valueOf(-1));
    }
}
