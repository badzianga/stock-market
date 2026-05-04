package com.badzianga.stockmarket.service;

import com.badzianga.stockmarket.model.TransactionType;
import com.badzianga.stockmarket.model.entity.AuditLog;
import com.badzianga.stockmarket.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private GetAuditLogService getAuditLogService;

    @Test
    void shouldReturnLogsSortedByTimestampAscending() {
        // given
        AuditLog log1 = AuditLog.builder()
                .id(1L)
                .type(TransactionType.buy)
                .walletId("wallet1")
                .stockName("stock1")
                .timestamp(Instant.parse("2026-05-03T20:00:00Z"))
                .build();

        AuditLog log2 = AuditLog.builder()
                .id(2L)
                .type(TransactionType.sell)
                .walletId("wallet2")
                .stockName("stock2")
                .timestamp(Instant.parse("2026-05-03T21:00:00Z"))
                .build();

        List<AuditLog> logs = List.of(log1, log2);

        when(auditLogRepository.findAllByOrderByTimestampAsc()).thenReturn(logs);

        // when
        List<AuditLog> result = getAuditLogService.getLogs();

        // then
        assertEquals(logs, result);
        verify(auditLogRepository).findAllByOrderByTimestampAsc();
    }
}
