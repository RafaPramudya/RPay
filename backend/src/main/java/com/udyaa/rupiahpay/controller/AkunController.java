package com.udyaa.rupiahpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udyaa.rupiahpay.dto.CreateAkun;
import com.udyaa.rupiahpay.service.AkunService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/api/akun")
public class AkunController {
    @Autowired
    private final AkunService service;

    @PostMapping("/create")
    public ResponseEntity<String> createAkun(@RequestBody CreateAkun akun) {
        try {
            service.createAccount(akun);
            return new ResponseEntity<>(HttpStatus.OK); 
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
}
