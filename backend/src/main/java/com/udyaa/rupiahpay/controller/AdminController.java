package com.udyaa.rupiahpay.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udyaa.rupiahpay.dto.ModifyRekening;
import com.udyaa.rupiahpay.dto.ResponseAkun;
import com.udyaa.rupiahpay.service.AkunService;
import com.udyaa.rupiahpay.service.TransactionService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AkunService akunService;
    private final TransactionService transactionService;

    @GetMapping("/user")
    public ResponseEntity<ResponseAkun> getMethodName(@RequestBody String email) {
        ResponseAkun akun = akunService.getAccountByEmail(email);

        return ResponseEntity.ok(akun);
    }


    @PostMapping("/deposit")
    public ResponseEntity<?> depositUang(@RequestBody ModifyRekening request) {
        try {
            transactionService.deposit(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<?> postMethodName(@RequestBody ModifyRekening request) {
        try {
            transactionService.withdraw(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
