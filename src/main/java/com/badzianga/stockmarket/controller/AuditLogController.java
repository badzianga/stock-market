package com.badzianga.stockmarket.controller;

import com.badzianga.stockmarket.mapper.AuditLogMapper;
import com.badzianga.stockmarket.model.dto.AuditLogDto;
import com.badzianga.stockmarket.model.dto.LogsResponse;
import com.badzianga.stockmarket.service.GetAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class AuditLogController {
    private final GetAuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<LogsResponse> getLogs() {
        List<AuditLogDto> logs = auditLogService.getLogs().stream()
                .map(AuditLogMapper::toDto)
                .toList();
        return ResponseEntity.ok(new LogsResponse(logs));
    }
}
