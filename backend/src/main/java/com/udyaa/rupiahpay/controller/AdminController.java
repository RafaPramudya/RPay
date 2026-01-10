package com.udyaa.rupiahpay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udyaa.rupiahpay.entity.Akun;
import com.udyaa.rupiahpay.service.AkunService;

import lombok.AllArgsConstructor;



@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AkunService akunService;

    @GetMapping("/user")
    public ResponseEntity<Akun> getMethodName(@RequestBody String email) {
        Akun akun = akunService.getAccountByEmail(email);

        return ResponseEntity.ok(akun);
    }
}
