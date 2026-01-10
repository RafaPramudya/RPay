package com.udyaa.rupiahpay.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.RegisterAkun;
import com.udyaa.rupiahpay.dto.ResponseAkun;
import com.udyaa.rupiahpay.dto.RestrictedResponseRekening;
import com.udyaa.rupiahpay.entity.Akun;
import com.udyaa.rupiahpay.entity.Rekening;
import com.udyaa.rupiahpay.enums.AkunRole;
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
                .role(AkunRole.USER)
                .build();
            
            akun.setBalance(Rekening.builder()
                .owner(akun)
                .build()
            );

            akunRepository.save(akun);
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseAkun getAccountByEmail(String email) throws UsernameNotFoundException {
        Akun akun = akunRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        Rekening rekening = akun.getBalance();

        ResponseAkun response = ResponseAkun.builder()
            .uuid(akun.getUuid())
            .firstName(akun.getFirstName())
            .lastName(akun.getLastName())
            .email(akun.getEmail())
            .password(akun.getPassword())
            .createdAt(akun.getCreatedAt())
            .role(akun.getRole())
            .balance(
                RestrictedResponseRekening.builder()
                .rekId(rekening.getRekId())
                .balance(rekening.getBalance())
                .currency(rekening.getCurrency())
                .build()
            )
            .build();

        return response;
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
                    .role(AkunRole.ADMIN)
                    .build();

                akun.setBalance(Rekening.builder()
                    .owner(akun)
                    .balance(new BigDecimal(271000000000000L))
                    .build()
                );
                akunRepository.save(akun);
            } else {
                throw new Exception("Unrecognized Patterns");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
