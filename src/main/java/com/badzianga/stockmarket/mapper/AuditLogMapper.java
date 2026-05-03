package com.badzianga.stockmarket.mapper;

import com.badzianga.stockmarket.model.dto.AuditLogDto;
import com.badzianga.stockmarket.model.entity.AuditLog;

public class AuditLogMapper {
    public static AuditLogDto toDto(AuditLog log) {
        return new AuditLogDto(
                log.getType(),
                log.getWalletId(),
                log.getStockName()
        );
    }
}
