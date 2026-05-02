package com.badzianga.stockmarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaos")
public class ChaosController {

    @PostMapping
    public ResponseEntity<Void> killInstance() {
        System.exit(0);
        return ResponseEntity.ok().build();
    }
}
