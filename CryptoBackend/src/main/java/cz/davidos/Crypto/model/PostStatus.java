package cz.davidos.Crypto.model;

public class PostStatus extends Crypto{
    private String err;
    public PostStatus(String name, String symbol, double quantity, String err) {
        super(name, symbol, quantity);
        this.err = err;
    }
}
