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
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        }).start();

        return ResponseEntity.ok().build();
    }
}
