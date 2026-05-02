package com.badzianga.stockmarket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankStockDto {
    private String name;
    private int quantity;
}
