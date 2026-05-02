package com.badzianga.stockmarket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private String id;
    private List<BankStockDto> stocks;
}
