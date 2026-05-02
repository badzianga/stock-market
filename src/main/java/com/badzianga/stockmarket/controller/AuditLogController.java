package com.badzianga.stockmarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class AuditLogController {

    @GetMapping
    public ResponseEntity<?> getLogs() {
        return ResponseEntity.internalServerError().build();
    }
}
