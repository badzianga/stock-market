package com.badzianga.stockmarket.service;

import com.badzianga.stockmarket.exception.StockDoesntExistException;
import com.badzianga.stockmarket.exception.StockNotAvailableException;
import com.badzianga.stockmarket.model.TransactionType;
import com.badzianga.stockmarket.model.entity.AuditLog;
import com.badzianga.stockmarket.model.entity.BankStock;
import com.badzianga.stockmarket.model.entity.Wallet;
import com.badzianga.stockmarket.model.entity.WalletStock;
import com.badzianga.stockmarket.repository.AuditLogRepository;
import com.badzianga.stockmarket.repository.BankStockRepository;
import com.badzianga.stockmarket.repository.WalletRepository;
import com.badzianga.stockmarket.repository.WalletStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final BankStockRepository bankStockRepository;
    private final WalletStockRepository walletStockRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void handleSellOrBuy(TransactionType transactionType, String walletId, String stockName) {
        switch (transactionType) {
            case buy -> buyStock(walletId, stockName);
            case sell -> sellStock(walletId, stockName);
        }
        addToAuditLog(transactionType, walletId, stockName);
    }

    public List<WalletStock> getWalletStocks(String walletId) {
        return walletStockRepository.findAllByWalletId(walletId);
    }

    public int getQuantity(String walletId, String stockName) {
        WalletStock stock = walletStockRepository.findByWalletIdAndStockName(walletId, stockName)
                .orElse(WalletStock.builder().quantity(0).build());
        return stock.getQuantity();
    }

    private void buyStock(String walletId, String stockName) {
        BankStock stock = bankStockRepository.findByName(stockName)
                .orElseThrow(() -> new StockDoesntExistException("Stock " + stockName + " does not exist"));

        if (stock.getQuantity() <= 0) {
            throw new StockNotAvailableException("Stock " + stockName + " is not available at this moment");
        }

        if (walletRepository.findById(walletId).isEmpty()) {
            walletRepository.save(new Wallet(walletId));
        }

        walletStockRepository.findByWalletIdAndStockName(walletId, stockName)
                .ifPresentOrElse(walletStock -> {
                    walletStock.incrementQuantity();
                    walletStockRepository.save(walletStock);
                }, () -> {
                    WalletStock walletStock = WalletStock.builder()
                            .walletId(walletId)
                            .stockName(stockName)
                            .quantity(1)
                            .build();
                    walletStockRepository.save(walletStock);
                });
        stock.decrementQuantity();
        bankStockRepository.save(stock);
    }

    private void sellStock(String walletId, String stockName) {
        BankStock stock = bankStockRepository.findByName(stockName)
                .orElseThrow(() -> new StockDoesntExistException("Stock " + stockName + " does not exist"));

        if (walletRepository.findById(walletId).isEmpty()) {
            walletRepository.save(new Wallet(walletId));
            throw new StockNotAvailableException("Wallet does not contain stock " + stockName);
        }

        walletStockRepository.findByWalletIdAndStockName(walletId, stockName)
                .ifPresentOrElse(walletStock -> {
                    if (walletStock.getQuantity() <= 0) {
                        throw new StockNotAvailableException("Wallet does not contain stock " + stockName);
                    }
                    walletStock.decrementQuantity();
                    walletStockRepository.save(walletStock);
                    stock.incrementQuantity();
                    bankStockRepository.save(stock);
                }, () -> {
                    throw new StockNotAvailableException("Wallet does not contain stock " + stockName);
                });
    }

    private void addToAuditLog(TransactionType transactionType, String walletId, String stockName) {
        AuditLog log = AuditLog.builder()
                .type(transactionType)
                .walletId(walletId)
                .stockName(stockName)
                .timestamp(Instant.now())
                .build();
        auditLogRepository.save(log);
    }
}
