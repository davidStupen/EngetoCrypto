package cz.davidos.Crypto.model;

import cz.davidos.Crypto.model.bitcoin.BitcoinDTO;
import cz.davidos.Crypto.model.dogecoin.DogeCoinDTO;
import cz.davidos.Crypto.model.ethereum.EthereumDTO;
import cz.davidos.Crypto.model.solana.SolanaDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;
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
        if (symbol == null) return BigDecimal.valueOf(-1);
        final String KEY = "CG-XYiAwrdRgKxfMybPHZuXceGG";

        final String URL_ETH = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd&x_cg_demo_api_key=" + KEY;
        final String URL_BTC = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd&x_cg_demo_api_key=" + KEY;
        final String URL_SOL = "https://api.coingecko.com/api/v3/simple/price?ids=solana&vs_currencies=usd&x_cg_demo_api_key=" + KEY;
        final String URL_DOGE = "https://api.coingecko.com/api/v3/simple/price?ids=dogecoin&vs_currencies=usd&x_cg_demo_api_key=" + KEY;
        RestTemplate template = new RestTemplate();
        double value = switch (symbol.toUpperCase()){
            case "BTC" -> template.getForObject(URL_BTC, BitcoinDTO.class).bitcoin().usd();
            case "ETH" -> template.getForObject(URL_ETH, EthereumDTO.class).ethereum().usd();
            case "SOL" -> template.getForObject(URL_SOL, SolanaDTO.class).solana().usd();
            case "DOGE" -> template.getForObject(URL_DOGE, DogeCoinDTO.class).dogecoin().usd();
            default -> -1;
        };
        return BigDecimal.valueOf(value);
    }
}
