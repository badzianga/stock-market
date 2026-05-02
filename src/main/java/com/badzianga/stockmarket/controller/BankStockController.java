package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.mapper.BankStockMapper;
import com.badzianga.stockmarket.model.dto.BankState;
import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.service.BankStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class BankStockController {
    private final BankStockService bankStockService;

    @GetMapping
    public ResponseEntity<BankState> getBankState() {
        List<BankStockDto> dtos = bankStockService.getAllStocks().stream()
                .map(BankStockMapper::toDto)
                .toList();
        BankState state = new BankState(dtos);

        return ResponseEntity.ok(state);
    }

    @PostMapping
    public ResponseEntity<?> setBankState(@RequestBody BankState state) {
        bankStockService.setStocks(state.getStocks());
        return ResponseEntity.ok().build();
    }
}
