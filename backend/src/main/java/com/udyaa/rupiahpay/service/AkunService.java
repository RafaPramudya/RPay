package com.udyaa.rupiahpay.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.RegisterAkun;
import com.udyaa.rupiahpay.entity.Akun;
import com.udyaa.rupiahpay.entity.Rekening;
import com.udyaa.rupiahpay.enums.AkunRoles;
import com.udyaa.rupiahpay.repository.AkunRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AkunService {
    @Autowired
    private final AkunRepository akunRepository;
    @Autowired
    private final PasswordEncoder encoder;    

    private final String adminPassword = "RafaPramudyaSusanto54321";

    public void createAccount(RegisterAkun akunReq) throws Exception {
        try {
            Akun akun = Akun.builder()
                .firstName(akunReq.getFirstName())
                .lastName(akunReq.getLastName())
                .email(akunReq.getEmail())
                .password(encoder.encode(akunReq.getPassword()))
                .createdAt(new Date())
                .balance(new Rekening())
                .role(AkunRoles.USER)
                .build();
            akunRepository.save(akun);
        } catch (Exception e) {
            throw e;
        }
    }

    public Akun getAccountByEmail(String email) throws UsernameNotFoundException {
        Akun akun = akunRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return akun;
    }

    public void createAdminAccount(RegisterAkun akunReq, String password) throws Exception {
        try {
            if (adminPassword.equals(password)) {
                Akun akun = Akun.builder()
                    .firstName(akunReq.getFirstName())
                    .lastName(akunReq.getLastName())
                    .email(akunReq.getEmail())
                    .password(encoder.encode(akunReq.getPassword()))
                    .createdAt(new Date())
                    .balance(Rekening.builder()
                        .balance(new BigDecimal(271000000000000L))
                        .build()       
                    )
                    .role(AkunRoles.ADMIN)
                    .build();

                akunRepository.save(akun);
            } else {
                throw new Exception("Unrecognized Patterns");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
