package com.badzianga.stockmarket.repository;

import com.badzianga.stockmarket.model.entity.BankStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankStockRepository extends JpaRepository<BankStock, String> {
    Optional<BankStock> findByName(String name);
}
