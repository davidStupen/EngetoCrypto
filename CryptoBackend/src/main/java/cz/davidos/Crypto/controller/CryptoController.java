package cz.davidos.Crypto.controller;

import cz.davidos.Crypto.exception.NotFind;
import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.model.HttpStat;
import cz.davidos.Crypto.service.CryptoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<HttpStat> addCrypto(@Valid @RequestBody Crypto crypto, BindingResult result){
        HttpStat postStatus = new HttpStat(crypto.getName(), crypto.getSymbol(), crypto.getQuantity());
        if (result.hasErrors()) {
            String error = result.getFieldError().getDefaultMessage();
            postStatus.setErr(error);
            return new ResponseEntity<>(postStatus, org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return this.service.saveOrException(crypto, postStatus); //interaguje i s listem Crypto, pokud je vše v pořádku.
    }
    @GetMapping({"", "/"})
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
    public ResponseEntity<HttpStat> getCrypto(@PathVariable UUID id){
        try {
            Crypto crypto = this.service.getCryptoById(id);
            HttpStat httpStatus = new HttpStat(crypto.getName(), crypto.getSymbol(), crypto.getQuantity());
            return new ResponseEntity<>(httpStatus, org.springframework.http.HttpStatus.OK);
        } catch (NotFind e) {
            throw ;
        }
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
