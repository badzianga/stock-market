package com.badzianga.stockmarket.repository;

import com.badzianga.stockmarket.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
