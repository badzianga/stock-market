package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.model.dto.BankState;
import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.model.entity.BankStock;
import com.badzianga.stockmarket.service.BankStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankStockController.class)
class BankStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankStockService bankStockService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnBankState() throws Exception {
        // given
        List<BankStock> stocks = List.of(
                new BankStock("stock1", 10),
                new BankStock("stock2", 20)
        );

        when(bankStockService.getAllStocks()).thenReturn(stocks);

        // when & then
        mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks").isArray())
                .andExpect(jsonPath("$.stocks[0].name").value("stock1"))
                .andExpect(jsonPath("$.stocks[0].quantity").value(10))
                .andExpect(jsonPath("$.stocks[1].name").value("stock2"))
                .andExpect(jsonPath("$.stocks[1].quantity").value(20));
    }

    @Test
    void shouldReturnEmptyBankState() throws Exception {
        when(bankStockService.getAllStocks()).thenReturn(List.of());

        mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks").isEmpty());
    }

    @Test
    void shouldSetBankState() throws Exception {
        // given
        BankState state = new BankState(List.of(
                new BankStockDto("stock1", 10),
                new BankStockDto("stock2", 20)
        ));

        // when & then
        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(state)))
                .andExpect(status().isOk());

        verify(bankStockService).setStocks(argThat(list ->
                list.size() == 2 &&
                        list.getFirst().getName().equals("stock1") &&
                        list.get(1).getQuantity() == 20
        ));
    }

    @Test
    void shouldHandleEmptyStockList() throws Exception {
        BankState state = new BankState(List.of());

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(state)))
                .andExpect(status().isOk());

        verify(bankStockService).setStocks(List.of());
    }
}
