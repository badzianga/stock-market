package com.badzianga.stockmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    public static class Transaction { }

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<?> makeTransaction(@PathVariable("wallet_id") String walletId,
                                             @PathVariable("stock_name") String stockName,
                                             @RequestBody Transaction transaction) {
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<?> getWallet(@PathVariable("wallet_id") String walletId) {
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<?> getQuantity(@PathVariable("wallet_id") String walletId,
                                         @PathVariable("stock_name") String stockName) {
        return ResponseEntity.internalServerError().build();
    }
}
