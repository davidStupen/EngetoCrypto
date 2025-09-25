package cz.davidos.Crypto.controller;

import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.service.CryptoService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    public List<Crypto> getAllCryptos(@RequestParam(required = false) String sort){
        return switch (sort){
            case null -> this.service.getAllCryptos();
            case "price" -> this.service.sortByPrice();
            case "name" -> this.service.sortByName();
            case "quantity" -> this.service.sortByQuantity();
            default -> this.service.getAllCryptos();
        };
    }
    @GetMapping("/{id}")
    public Crypto getCrypto(@PathVariable UUID id){
        return this.service.getCryptoById(id);
    }
    @PutMapping("/{id}")
    public void updateCrypto(@RequestBody Crypto crypto, @PathVariable UUID id){
        this.service.updateCrypto(id, crypto);
    }
    @GetMapping("/portfolio-value")
    public BigDecimal getTotalValue(){
        return this.service.countTotalValue();
    }
}
