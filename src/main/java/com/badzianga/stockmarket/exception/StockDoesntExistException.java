package com.badzianga.stockmarket.exception;

public class StockDoesntExistException extends RuntimeException {
    public StockDoesntExistException(String message) {
        super(message);
    }
}
