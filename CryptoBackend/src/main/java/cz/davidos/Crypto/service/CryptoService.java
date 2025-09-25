package cz.davidos.Crypto.service;

import cz.davidos.Crypto.model.Crypto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {
    List<Crypto> cryptoList = new ArrayList<>();

    public void addCrypto(Crypto crypto){
        this.cryptoList.add(new Crypto(crypto.getName(), crypto.getSymbol(), crypto.getQuantity()));
    }

}
