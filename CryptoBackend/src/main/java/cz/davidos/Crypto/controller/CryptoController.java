package cz.davidos.Crypto.controller;

import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.service.CryptoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/cryptos")
public class CryptoController {
    private CryptoService service;

    public CryptoController(CryptoService service) {
        this.service = service;
    }

    @PostMapping
    public void addCrypto(@RequestBody Crypto crypto){
        this.service.addCrypto(crypto);
    }
    @GetMapping
    public List<Crypto> getAllCryptos(@RequestParam String sort){
        return switch (sort.toLowerCase()){
            case "price" -> this.service.sortByPrice();
            case "name" -> this.service.sortByName();
            case "quantity" -> this.service.sortByQuantity();
            case null -> this.service.getAllCryptos();
            default -> this.service.getAllCryptos();
        };
    }
}
