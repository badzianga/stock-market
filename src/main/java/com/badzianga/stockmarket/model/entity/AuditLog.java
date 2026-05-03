package com.badzianga.stockmarket.model.entity;

import com.badzianga.stockmarket.model.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "wallet_id", nullable = false)
    private String walletId;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
