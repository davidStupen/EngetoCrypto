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
    public List<Crypto> getAllCryptos(){
        return this.service.getAllCryptos();
    }
}
