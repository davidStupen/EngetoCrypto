package cz.davidos.Crypto.model;

import cz.davidos.Crypto.model.bitcoin.BitcoinDTO;
import cz.davidos.Crypto.model.ethereum.Ethereum;
import cz.davidos.Crypto.model.ethereum.EthereumDTO;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

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
        this.price = this.currentPrice(symbol);
        this.quantity = quantity;
    }
    private BigDecimal currentPrice(String symbol){
        final String URL_ETH = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd";
        final String URL_BTC = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
        RestTemplate template = new RestTemplate();
        double value = switch (symbol.toUpperCase()){
            case "BTC" -> template.getForObject(URL_BTC, BitcoinDTO.class).bitcoin().usd();
            case "ETH" -> template.getForObject(URL_ETH, EthereumDTO.class).ethereum().usd();
            default -> -1;
        };
        return BigDecimal.valueOf(value);
    }
}
