package com.udyaa.rupiahpay.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.CreateAkun;
import com.udyaa.rupiahpay.entity.Akun;
import com.udyaa.rupiahpay.repository.AkunRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AkunService {
    @Autowired
    private final AkunRepository akunRepository;
    @Autowired
    private final PasswordEncoder encoder;    

    public void createAccount(CreateAkun akunReq) throws Exception {
        try {
            Akun akun = new Akun();
            akun.setFirstName(akunReq.getFirstName());
            akun.setLastName(akunReq.getLastName());
            akun.setEmail(akunReq.getEmail());
            akun.setPassword(encoder.encode(akunReq.getPassword()));
            akun.setCreatedAt(new Date());

            akunRepository.save(akun);
        } catch (Exception e) {
            throw e;
        }
    } 
}
