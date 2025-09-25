package cz.davidos.Crypto.service;

import cz.davidos.Crypto.model.Crypto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
}
