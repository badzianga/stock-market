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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private BankStockRepository bankStockRepository;
    @Mock
    private WalletStockRepository walletStockRepository;
    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldBuyStockAndCreateWalletAndWalletStock() {
        // given
        String walletId = "wallet1";
        String stockName = "stock1";

        BankStock bankStock = new BankStock(stockName, 99);

        when(bankStockRepository.findByName(stockName)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        when(walletStockRepository.findByWalletIdAndStockName(walletId, stockName))
                .thenReturn(Optional.empty());

        // when
        walletService.handleSellOrBuy(TransactionType.buy, walletId, stockName);

        // then
        verify(walletRepository).save(any(Wallet.class));
        verify(walletStockRepository).save(argThat(ws ->
                ws.getWalletId().equals(walletId)
                        && ws.getStockName().equals(stockName)
                        && ws.getQuantity() == 1
        ));
        verify(bankStockRepository).save(bankStock);
        verify(auditLogRepository).save(any(AuditLog.class));

        assertEquals(98, bankStock.getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenBuyingExistingWalletStock() {
        // given
        String walletId = "wallet1";
        String stockName = "stock1";

        BankStock bankStock = new BankStock(stockName, 99);
        WalletStock walletStock = WalletStock.builder()
                .walletId(walletId)
                .stockName(stockName)
                .quantity(2)
                .build();

        when(bankStockRepository.findByName(stockName)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId)));
        when(walletStockRepository.findByWalletIdAndStockName(walletId, stockName))
                .thenReturn(Optional.of(walletStock));

        // when
        walletService.handleSellOrBuy(TransactionType.buy, walletId, stockName);

        // then
        assertEquals(3, walletStock.getQuantity());
        assertEquals(98, bankStock.getQuantity());

        verify(walletStockRepository).save(walletStock);
        verify(bankStockRepository).save(bankStock);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void shouldThrowWhenStockDoesNotExistOnBuy() {
        when(bankStockRepository.findByName("stock1")).thenReturn(Optional.empty());

        assertThrows(StockDoesntExistException.class,
                () -> walletService.handleSellOrBuy(TransactionType.buy, "wallet1", "stock1"));

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenStockNotAvailableOnBuy() {
        BankStock bankStock = new BankStock("stock1", 0);
        when(bankStockRepository.findByName("stock1")).thenReturn(Optional.of(bankStock));

        assertThrows(StockNotAvailableException.class,
                () -> walletService.handleSellOrBuy(TransactionType.buy, "wallet1", "stock1"));

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void shouldSellStockSuccessfully() {
        String walletId = "wallet1";
        String stockName = "stock1";

        BankStock bankStock = new BankStock(stockName, 1);
        WalletStock walletStock = WalletStock.builder()
                .walletId(walletId)
                .stockName(stockName)
                .quantity(2)
                .build();

        when(bankStockRepository.findByName(stockName)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId)));
        when(walletStockRepository.findByWalletIdAndStockName(walletId, stockName))
                .thenReturn(Optional.of(walletStock));

        walletService.handleSellOrBuy(TransactionType.sell, walletId, stockName);

        assertEquals(1, walletStock.getQuantity());
        assertEquals(2, bankStock.getQuantity());

        verify(walletStockRepository).save(walletStock);
        verify(bankStockRepository).save(bankStock);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void shouldThrowWhenWalletDoesNotHaveStock() {
        String walletId = "wallet1";

        BankStock bankStock = new BankStock("stock1", 1);

        when(bankStockRepository.findByName("stock1")).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId)));
        when(walletStockRepository.findByWalletIdAndStockName(walletId, "stock1"))
                .thenReturn(Optional.empty());

        assertThrows(StockNotAvailableException.class,
                () -> walletService.handleSellOrBuy(TransactionType.sell, walletId, "stock1"));

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenWalletDoesNotExistOnSell() {
        when(bankStockRepository.findByName("stock1"))
                .thenReturn(Optional.of(new BankStock("stock1", 1)));
        when(walletRepository.findById("wallet1")).thenReturn(Optional.empty());

        assertThrows(StockNotAvailableException.class,
                () -> walletService.handleSellOrBuy(TransactionType.sell, "wallet1", "stock1"));

        verify(walletRepository).save(any(Wallet.class));
        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void shouldReturnWalletStocks() {
        List<WalletStock> stocks = List.of(new WalletStock());
        when(walletStockRepository.findAllByWalletId("wallet1")).thenReturn(stocks);

        List<WalletStock> result = walletService.getWalletStocks("wallet1");

        assertEquals(stocks, result);
    }

    @Test
    void shouldReturnQuantityWhenStockExists() {
        WalletStock stock = WalletStock.builder().quantity(5).build();
        when(walletStockRepository.findByWalletIdAndStockName("wallet1", "stock1"))
                .thenReturn(Optional.of(stock));

        int result = walletService.getQuantity("wallet1", "stock1");

        assertEquals(5, result);
    }

    @Test
    void shouldReturnZeroWhenStockDoesNotExist() {
        when(walletStockRepository.findByWalletIdAndStockName("wallet1", "stock1"))
                .thenReturn(Optional.empty());

        int result = walletService.getQuantity("wallet1", "stock1");

        assertEquals(0, result);
    }
}
