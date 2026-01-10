package com.udyaa.rupiahpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udyaa.rupiahpay.dto.LoginAkun;
import com.udyaa.rupiahpay.dto.RegisterAkun;
import com.udyaa.rupiahpay.dto.ResponseAkun;
import com.udyaa.rupiahpay.service.AkunService;
import com.udyaa.rupiahpay.service.JwtService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/akun")
public class AkunController {
    @Autowired
    private final AkunService akunService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterAkun akun) {
        try {
            akunService.createAccount(akun);
            return new ResponseEntity<>(HttpStatus.OK); 
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/auth/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterAkun akun, @RequestHeader("Must-Known-Thing") String password) {
        try {

            akunService.createAdminAccount(akun, password);
            return new ResponseEntity<>(HttpStatus.OK); 
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginAkun akun) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(akun.getEmail(), akun.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(akun.getEmail());
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>("Invalid User Requests", HttpStatus.UNAUTHORIZED);
        }
    }
    
    @GetMapping
    public ResponseEntity<ResponseAkun> getUser(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        ResponseAkun akun = akunService.getResponseAccountByEmail(email);

        return ResponseEntity.ok(akun);
    }
    
}
