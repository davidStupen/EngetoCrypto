package cz.davidos.Crypto.repository;

import cz.davidos.Crypto.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CryptoRepo extends JpaRepository<Crypto, UUID> {
}
