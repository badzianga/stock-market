package com.badzianga.stockmarket.model.dto;

import com.badzianga.stockmarket.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {
    private TransactionType type;
    private String walletId;
    private String stockName;
}
