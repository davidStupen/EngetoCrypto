package cz.davidos.Crypto.model;

import lombok.Setter;

@Setter
public class HttpStatus extends Crypto{
    private String err;
    public HttpStatus(String name, String symbol, double quantity, String err) {
        super(name, symbol, quantity);
        this.err = err;
    }
    public HttpStatus(String name, String symbol, double quantity) {
        super(name, symbol, quantity);
    }
}
