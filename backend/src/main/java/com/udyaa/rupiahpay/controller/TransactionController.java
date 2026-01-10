package com.udyaa.rupiahpay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udyaa.rupiahpay.dto.RequestTransfer;
import com.udyaa.rupiahpay.service.JwtService;
import com.udyaa.rupiahpay.service.TransactionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transfer")
public class TransactionController {
    private final TransactionService transactionService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody RequestTransfer transfer, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        transactionService.transfer(transfer, email);

        return ResponseEntity.ok(null);
    } 
}
