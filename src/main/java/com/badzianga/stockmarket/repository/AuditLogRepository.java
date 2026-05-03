package com.badzianga.stockmarket.repository;

import com.badzianga.stockmarket.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByTimestampAsc();
}
