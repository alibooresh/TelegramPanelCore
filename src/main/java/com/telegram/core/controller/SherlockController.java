package com.telegram.core.controller;

import com.telegram.core.service.SherlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sherlock")
public class SherlockController {
    private final SherlockService sherlockService;

    public SherlockController(SherlockService sherlockService) {
        this.sherlockService = sherlockService;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scan(String username) throws IOException, InterruptedException {
        return ResponseEntity.ok(sherlockService.scan(username));
    }
}
