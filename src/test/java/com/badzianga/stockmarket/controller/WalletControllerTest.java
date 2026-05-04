package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.exception.StockDoesntExistException;
import com.badzianga.stockmarket.exception.StockNotAvailableException;
import com.badzianga.stockmarket.model.TransactionType;
import com.badzianga.stockmarket.model.dto.TransactionRequest;
import com.badzianga.stockmarket.model.entity.WalletStock;
import com.badzianga.stockmarket.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WalletService walletService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldMakeTransactionSuccessfully() throws Exception {
        // given
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.buy);

        // when & then
        mockMvc.perform(post("/wallets/wallet1/stocks/stock1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(walletService).handleSellOrBuy(TransactionType.buy, "wallet1", "stock1");
    }

    @Test
    void shouldReturn404WhenStockDoesNotExist() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.buy);

        doThrow(new StockDoesntExistException("Not found"))
                .when(walletService)
                .handleSellOrBuy(any(), any(), any());

        mockMvc.perform(post("/wallets/wallet1/stocks/stock1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenStockNotAvailable() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.sell);

        doThrow(new StockNotAvailableException("Not available"))
                .when(walletService)
                .handleSellOrBuy(any(), any(), any());

        mockMvc.perform(post("/wallets/wallet1/stocks/stock1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnWalletStocks() throws Exception {
        // given
        List<WalletStock> stocks = List.of(
                WalletStock.builder().walletId("wallet1").stockName("stock1").quantity(2).build(),
                WalletStock.builder().walletId("wallet1").stockName("ING").quantity(3).build()
        );

        when(walletService.getWalletStocks("wallet1")).thenReturn(stocks);

        // when & then
        mockMvc.perform(get("/wallets/wallet1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wallet1"))
                .andExpect(jsonPath("$.stocks[0].name").value("stock1"))
                .andExpect(jsonPath("$.stocks[0].quantity").value(2))
                .andExpect(jsonPath("$.stocks[1].name").value("ING"))
                .andExpect(jsonPath("$.stocks[1].quantity").value(3));
    }

    @Test
    void shouldReturnEmptyWallet() throws Exception {
        when(walletService.getWalletStocks("wallet1")).thenReturn(List.of());

        mockMvc.perform(get("/wallets/wallet1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks").isEmpty());
    }

    @Test
    void shouldReturnQuantity() throws Exception {
        when(walletService.getQuantity("wallet1", "stock1")).thenReturn(5);

        mockMvc.perform(get("/wallets/wallet1/stocks/stock1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void shouldReturnZeroQuantity() throws Exception {
        when(walletService.getQuantity("wallet1", "stock1")).thenReturn(0);

        mockMvc.perform(get("/wallets/wallet1/stocks/stock1"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}
