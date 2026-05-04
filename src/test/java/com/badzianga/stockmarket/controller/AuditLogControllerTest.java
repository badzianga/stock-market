package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.model.TransactionType;
import com.badzianga.stockmarket.model.entity.AuditLog;
import com.badzianga.stockmarket.service.GetAuditLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAuditLogService auditLogService;

    @Test
    void shouldReturnAuditLogs() throws Exception {
        // given
        AuditLog log = AuditLog.builder()
                .id(1L)
                .type(TransactionType.buy)
                .walletId("wallet1")
                .stockName("stock1")
                .timestamp(Instant.parse("2026-05-03T20:00:00Z"))
                .build();

        when(auditLogService.getLogs()).thenReturn(List.of(log));

        // when & then
        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.log").isArray())
                .andExpect(jsonPath("$.log[0].type").value("buy"))
                .andExpect(jsonPath("$.log[0].walletId").value("wallet1"))
                .andExpect(jsonPath("$.log[0].stockName").value("stock1"));
    }

    @Test
    void shouldReturnEmptyListWhenNoLogs() throws Exception {
        when(auditLogService.getLogs()).thenReturn(List.of());

        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.log").isEmpty());
    }
}
