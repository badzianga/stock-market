package com.badzianga.stockmarket.model.dto;

import com.badzianga.stockmarket.model.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    TransactionType type;
}
