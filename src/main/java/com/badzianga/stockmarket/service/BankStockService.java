package com.badzianga.stockmarket.service;

import com.badzianga.stockmarket.mapper.BankStockMapper;
import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.model.entity.BankStock;
import com.badzianga.stockmarket.repository.BankStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankStockService {
    private final BankStockRepository bankStockRepository;

    public void setStocks(List<BankStockDto> stocks) {
        bankStockRepository.deleteAll();

        List<BankStock> entities = stocks.stream()
                .map(BankStockMapper::toEntity)
                .toList();
        bankStockRepository.saveAll(entities);
    }

    public List<BankStock> getAllStocks() {
         return bankStockRepository.findAll();

    }
}
