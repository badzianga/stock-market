package com.badzianga.stockmarket.repository;

import com.badzianga.stockmarket.model.entity.BankStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankStockRepository extends JpaRepository<BankStock, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BankStock> findByName(String name);
}
