package com.badzianga.stockmarket.service;

import com.badzianga.stockmarket.model.entity.AuditLog;
import com.badzianga.stockmarket.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAuditLogService {
    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getLogs() {
        return auditLogRepository.findAllByOrderByTimestampAsc();
    }
}
