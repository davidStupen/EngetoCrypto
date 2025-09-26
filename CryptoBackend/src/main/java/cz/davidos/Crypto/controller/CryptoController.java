package cz.davidos.Crypto.controller;

import cz.davidos.Crypto.exception.NotFind;
import cz.davidos.Crypto.exception.TotalValue;
import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.model.HttpStat;
import cz.davidos.Crypto.model.TotalValueDTO;
import cz.davidos.Crypto.service.CryptoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
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
        return this.service.saveOrException(crypto, postStatus);
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
            return new ResponseEntity<>(httpStatus, HttpStatus.OK);
        } catch (NotFind e) {
            HttpStat httpStatus = new HttpStat(null, null, -1, e.getMessage());
            return new ResponseEntity<>(httpStatus, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<HttpStat> updateCrypto(@Valid @RequestBody Crypto crypto, BindingResult result, @PathVariable UUID id){
        if (result.hasErrors()){
            String error = result.getFieldError().getDefaultMessage();
            HttpStat httpStat = new HttpStat(crypto.getName(), crypto.getSymbol(), crypto.getQuantity(), error);
            return new ResponseEntity<>(httpStat, HttpStatus.BAD_REQUEST);
        }
        return this.service.updateCrypto(id, crypto);
    }
    @GetMapping("/portfolio-value")
    public TotalValueDTO getTotalValue(){
        try {
            BigDecimal totalValue = this.service.countTotalValue();
            return new TotalValueDTO(null, totalValue);
        } catch (TotalValue e) {
            return new TotalValueDTO(e.getMessage());
        }
    }
}
