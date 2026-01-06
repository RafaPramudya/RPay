package com.udyaa.rupiahpay.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void createAccount(CreateAkun akunReq) throws Exception {
        try {
            Akun akun = new Akun();
            akun.setFirstName(akunReq.getFirstName());
            akun.setLastName(akunReq.getLastName());
            akun.setEmail(akunReq.getEmail());
            akun.setPassword(akunReq.getPassword());
            akun.setCreatedAt(new Date(System.currentTimeMillis()));

            akunRepository.save(akun);
        } catch (Exception e) {
            throw e;
        }
    } 
}
