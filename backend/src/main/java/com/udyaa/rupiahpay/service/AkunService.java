package com.udyaa.rupiahpay.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.CreateAkun;
import com.udyaa.rupiahpay.entity.Akun;
import com.udyaa.rupiahpay.entity.SaldoAkun;
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

    public void createAccount(CreateAkun akunReq) throws Exception {
        try {
            Akun akun = Akun.builder()
                .firstName(akunReq.getFirstName())
                .lastName(akunReq.getLastName())
                .email(akunReq.getEmail())
                .password(encoder.encode(akunReq.getPassword()))
                .createdAt(new Date())
                .balance(new SaldoAkun())
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
}
