package cz.davidos.Crypto.model;

import lombok.Setter;

@Setter
public class HttpStat extends Crypto{
    private String err;
    public HttpStat(String name, String symbol, double quantity, String err) {
        super(name, symbol, quantity);
        this.err = err;
    }
    public HttpStat(String name, String symbol, double quantity) {
        super(name, symbol, quantity);
    }
}
