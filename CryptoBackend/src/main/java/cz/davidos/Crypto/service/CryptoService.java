package cz.davidos.Crypto.service;

import cz.davidos.Crypto.exception.NotFind;
import cz.davidos.Crypto.exception.TotalValue;
import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.model.HttpStat;
import cz.davidos.Crypto.repository.CryptoRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CryptoService {
    private CryptoRepo repo;

    public CryptoService(CryptoRepo repo) {
        this.repo = repo;
    }

    public ResponseEntity<HttpStat> saveOrException(Crypto crypto, HttpStat postStatus){
        if (!crypto.getSymbol().equalsIgnoreCase("BTC") &&
                !crypto.getSymbol().equalsIgnoreCase("ETH") &&
                !crypto.getSymbol().equalsIgnoreCase("SOL") &&
                !crypto.getSymbol().equalsIgnoreCase("DOGE")){
            postStatus.setErr("symbol musí obsahovat BTC nebo ETH nebo SOL nebo DOGE. Jiná možnost není možna. Nebylo uloženo! Není povoleno: " + crypto.getSymbol());
            return new ResponseEntity<>(postStatus, org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        crypto.setName(crypto.getName().toLowerCase());
        this.addCrypto(crypto);
        return new ResponseEntity<>(postStatus, org.springframework.http.HttpStatus.OK);
    }

    private void addCrypto(Crypto crypto){
        this.repo.save(new Crypto(crypto.getName(), crypto.getSymbol(), crypto.getQuantity()));
    }

    public List<Crypto> getAllCryptos(){
        return this.repo.findAll();
    }

    public List<Crypto> sortByPrice(){
        List<Crypto> cryptos = this.repo.findAll();
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getPrice().doubleValue() > o2.getPrice().doubleValue()) return 1;
            if (o1.getPrice().doubleValue() < o2.getPrice().doubleValue()) return -1;
            return 0;
        };
        cryptos.sort(com);
        return cryptos;
    }

    public List<Crypto> sortByName(){
        List<Crypto> cryptos = this.repo.findAll();
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getName().compareTo(o2.getName()) > 0) return 1;
            if (o1.getName().compareTo(o2.getName()) < 0) return -1;
            return 0;
        };
        cryptos.sort(com);
        return cryptos;
    }

    public List<Crypto> sortByQuantity(){
        List<Crypto> cryptos = this.repo.findAll();
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getQuantity() > o2.getQuantity()) return 1;
            if (o1.getQuantity() < o2.getQuantity()) return -1;
            return 0;
        };
        cryptos.sort(com);
        return cryptos;
    }

    public Crypto getCryptoById(UUID id) throws NotFind {
        return this.repo.findById(id).orElseThrow(() -> new NotFind("nenalezeno id " + id + ". V seznamu se nic nezměnilo."));
    }

    public ResponseEntity<HttpStat> updateCrypto(UUID id, Crypto crypto) {
        try {
            this.getCryptoById(id);
        } catch (NotFind e) {
            return new ResponseEntity<>(new HttpStat(null, null, -1, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        if (!crypto.getSymbol().equalsIgnoreCase("BTC") &&
                !crypto.getSymbol().equalsIgnoreCase("ETH") &&
                !crypto.getSymbol().equalsIgnoreCase("SOL") &&
                !crypto.getSymbol().equalsIgnoreCase("DOGE")){
            String err = "symbol musí obsahovat BTC nebo ETH nebo SOL nebo DOGE. Jiná možnost není možna. Nebylo uloženo! Není povoleno: " + crypto.getSymbol();
            HttpStat httpStat = new HttpStat(crypto.getName(), crypto.getSymbol(), crypto.getQuantity(), err);
            return new ResponseEntity<>(httpStat, HttpStatus.BAD_REQUEST);
        }
        Crypto crypto1 = new Crypto(crypto.getName().toLowerCase(), crypto.getSymbol(), crypto.getQuantity());
        crypto1.setId(id);
        this.repo.save(crypto1);
        HttpStat httpStat = new HttpStat(crypto.getName(), crypto.getSymbol(), crypto.getQuantity());
        return new ResponseEntity<>(httpStat, HttpStatus.OK);
    }

    public BigDecimal countTotalValue() throws TotalValue {
        if (this.repo.count() == 0) throw new TotalValue("Tvoje portfolio je zatím prázdné!");
        BigDecimal value = BigDecimal.valueOf(0);
        List<Crypto> cryptos = this.repo.findAll();
        for (Crypto item : cryptos){
            value = value.add(BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()));
        }
        return value;
    }

}
