package com.badzianga.stockmarket.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankStock {
    @Id
    @Column(name = "name", nullable = false, updatable = false)
    String name;

    @Column(name = "quantity", nullable = false)
    int quantity;
}
