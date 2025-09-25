package cz.davidos.Crypto.model;

import cz.davidos.Crypto.model.bitcoin.BitcoinDTO;
import cz.davidos.Crypto.model.ethereum.EthereumDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Crypto {
    private UUID id;
    @NotEmpty(message = "jmeno je povinný")
    private String name;
    @NotEmpty(message = "symbol je povinný")
    private String symbol;
    private BigDecimal price;
    @Min(value = 0, message = "hodnota nesmí být záporná")
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
