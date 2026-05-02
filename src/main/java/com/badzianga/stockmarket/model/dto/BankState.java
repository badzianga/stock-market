package com.badzianga.stockmarket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BankState {
    private List<BankStockDto> stocks;
}
