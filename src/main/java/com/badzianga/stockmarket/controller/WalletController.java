package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.exception.StockDoesntExistException;
import com.badzianga.stockmarket.exception.StockNotAvailableException;
import com.badzianga.stockmarket.mapper.BankStockMapper;
import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.model.dto.TransactionRequest;
import com.badzianga.stockmarket.model.dto.WalletDto;
import com.badzianga.stockmarket.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<?> makeTransaction(@PathVariable("wallet_id") String walletId,
                                             @PathVariable("stock_name") String stockName,
                                             @RequestBody TransactionRequest request) {
        try {
            walletService.handleSellOrBuy(request.getType(), walletId, stockName);
            return ResponseEntity.ok().build();
        } catch (StockDoesntExistException e) {
            return ResponseEntity.notFound().build();
        } catch (StockNotAvailableException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<?> getWallet(@PathVariable("wallet_id") String walletId) {
        List<BankStockDto> stocks = walletService.getWalletStocks(walletId).stream()
                .map(BankStockMapper::toDto)
                .toList();
        return ResponseEntity.ok(new WalletDto(walletId, stocks));
    }

    @GetMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<?> getQuantity(@PathVariable("wallet_id") String walletId,
                                         @PathVariable("stock_name") String stockName) {
        int quantity = walletService.getQuantity(walletId, stockName);
        return ResponseEntity.ok(quantity);
    }
}
