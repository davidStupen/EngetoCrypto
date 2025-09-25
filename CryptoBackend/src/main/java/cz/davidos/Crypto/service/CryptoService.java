package cz.davidos.Crypto.service;

import cz.davidos.Crypto.model.Crypto;
import cz.davidos.Crypto.model.PostStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CryptoService {
    List<Crypto> cryptoList = new ArrayList<>();

    public void addCrypto(Crypto crypto){
        this.cryptoList.add(new Crypto(crypto.getName(), crypto.getSymbol(), crypto.getQuantity()));
    }
    public List<Crypto> getAllCryptos(){
        return this.cryptoList;
    }
    public List<Crypto> sortByPrice(){
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getPrice().doubleValue() > o2.getPrice().doubleValue()) return 1;
            if (o1.getPrice().doubleValue() < o2.getPrice().doubleValue()) return -1;
            return 0;
        };
        this.cryptoList.sort(com);
        return this.cryptoList;
    }
    public List<Crypto> sortByName(){
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getName().compareTo(o2.getName()) > 0) return 1;
            if (o1.getName().compareTo(o2.getName()) < 0) return -1;
            return 0;
        };
        this.cryptoList.sort(com);
        return this.cryptoList;
    }
    public List<Crypto> sortByQuantity(){
        Comparator<Crypto> com = (o1, o2) -> {
            if (o1.getQuantity() > o2.getQuantity()) return 1;
            if (o1.getQuantity() < o2.getQuantity()) return -1;
            return 0;
        };
        this.cryptoList.sort(com);
        return this.cryptoList;
    }

    public Crypto getCryptoById(UUID id) {
        return this.cryptoList.stream().filter(item -> item.getId().equals(id))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("nenalezeno podle id " + id));
    }

    public void updateCrypto(UUID id, Crypto crypto) {
        for (int i = 0; i < this.cryptoList.size(); i++) {
            if (this.cryptoList.get(i).getId().equals(id)) {
                this.cryptoList.set(i, new Crypto(crypto.getName(), crypto.getSymbol(), crypto.getQuantity()));
                break;
            }
        }
    }

    public BigDecimal countTotalValue() {
        BigDecimal value = BigDecimal.valueOf(0);
        for (Crypto item : this.cryptoList){
            value = value.add(BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()));
        }
        return value;
    }

    public ResponseEntity<PostStatus> getException(Crypto crypto, PostStatus postStatus){
        if (!crypto.getSymbol().equalsIgnoreCase("BTC") &&
                !crypto.getSymbol().equalsIgnoreCase("ETH") &&
                !crypto.getSymbol().equalsIgnoreCase("SOL") &&
                !crypto.getSymbol().equalsIgnoreCase("DOGE")){
            postStatus.setErr("symbol musí obsahovat BTC nebo ETH nebo SOL nebo DOGE");
            return new ResponseEntity<>(postStatus, HttpStatus.NOT_FOUND);
        }
        this.addCrypto(crypto);
        return new ResponseEntity<>(postStatus, HttpStatus.OK);
    }
}
