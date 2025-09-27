package cz.davidos.Crypto.model;

import cz.davidos.Crypto.model.bitcoin.BitcoinDTO;
import cz.davidos.Crypto.model.dogecoin.DogeCoinDTO;
import cz.davidos.Crypto.model.ethereum.EthereumDTO;
import cz.davidos.Crypto.model.solana.SolanaDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Crypto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotEmpty(message = "jmeno je povinný")
    private String name;
    @NotEmpty(message = "symbol je povinný")
    private String symbol;
    private BigDecimal price;
    @Min(value = 0, message = "hodnota nesmí být záporná")
    private double quantity;

    public Crypto(String name, String symbol, double quantity) {
        this.name = name;
        this.symbol = symbol;
        this.price = this.currentPrice(symbol);
        this.quantity = quantity;
    }
    private BigDecimal currentPrice(String symbol){
        if (symbol == null) return BigDecimal.valueOf(-1);
        final String URL_ETH = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd";
        final String URL_BTC = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
        final String URL_SOL = "https://api.coingecko.com/api/v3/simple/price?ids=solana&vs_currencies=usd";
        final String URL_DOGE = "https://api.coingecko.com/api/v3/simple/price?ids=dogecoin&vs_currencies=usd";
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
