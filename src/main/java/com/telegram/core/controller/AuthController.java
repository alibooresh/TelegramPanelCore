
// AuthController.java
package com.telegram.core.controller;

import com.telegram.core.dto.ProxyDto;
import com.telegram.core.service.AuthService;
import org.drinkless.tdlib.TdApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-phone")
    public ResponseEntity<String> sendPhone(String phoneNumber) throws ExecutionException, InterruptedException {
        authService.sendPhoneNumber(phoneNumber);
        return ResponseEntity.ok("Phone sent.");
    }

    @PostMapping("/check-code")
    public ResponseEntity<String> checkCode(String code) throws ExecutionException, InterruptedException {
        authService.checkCode(code);
        return ResponseEntity.ok("Code checked.");
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> status(@RequestParam String userId) {
        return ResponseEntity.ok(authService.isAuthorized(userId));
    }

    @GetMapping("/setParam")
    public ResponseEntity<?> setParam() {
        authService.setParam();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(authService.logout());
    }

    @GetMapping("/close")
    public ResponseEntity<?> close() {
        return ResponseEntity.ok(authService.close());
    }

    @PostMapping("/setProxy")
    public ResponseEntity<?> setProxy(@RequestBody ProxyDto proxyDto) {
        authService.setProxy(proxyDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/disableAllProxies")
    public ResponseEntity<?> disableAllProxies() throws InterruptedException {
        authService.disableAllProxies();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
