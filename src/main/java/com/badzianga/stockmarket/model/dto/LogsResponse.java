package com.badzianga.stockmarket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LogsResponse {
    private List<AuditLogDto> log;
}
