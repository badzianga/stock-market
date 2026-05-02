package com.badzianga.stockmarket.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet_stocks", uniqueConstraints = @UniqueConstraint(columnNames = {"wallet_id", "stock_name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private String walletId;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
