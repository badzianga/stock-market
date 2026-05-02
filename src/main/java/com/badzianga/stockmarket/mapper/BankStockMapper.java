package com.badzianga.stockmarket.mapper;

import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.model.entity.BankStock;
import com.badzianga.stockmarket.model.entity.WalletStock;

public class BankStockMapper {

    public static BankStockDto toDto(BankStock stock) {
        return new BankStockDto(
                stock.getName(),
                stock.getQuantity()
        );
    }

    public static BankStockDto toDto(WalletStock stock) {
        return new BankStockDto(
                stock.getStockName(),
                stock.getQuantity()
        );
    }

    public static BankStock toEntity(BankStockDto dto) {
        return new BankStock(
                dto.getName(),
                dto.getQuantity()
        );
    }
}
