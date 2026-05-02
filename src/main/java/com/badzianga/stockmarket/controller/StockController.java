package com.badzianga.stockmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    public static class BankState { }

    @GetMapping
    public ResponseEntity<?> getBankState() {
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping
    public ResponseEntity<?> setBankState(@RequestBody BankState state) {
        return  ResponseEntity.internalServerError().build();
    }
}
