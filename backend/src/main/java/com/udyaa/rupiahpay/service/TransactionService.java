package com.udyaa.rupiahpay.service;

import java.math.BigDecimal;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.ModifyRekening;
import com.udyaa.rupiahpay.entity.Rekening;
import com.udyaa.rupiahpay.repository.AkunRepository;
import com.udyaa.rupiahpay.repository.RekeningRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TransactionService {
    private final AkunRepository akunRepository;
    private final RekeningRepository rekeningRepository;

    @Transactional
    public void deposit(ModifyRekening request) {
        Rekening rekening = rekeningRepository.findByRekId(request.getRekId())
            .orElseThrow(() -> new UsernameNotFoundException(request.getRekId()));
        
        BigDecimal jumlah = rekening.getBalance();
        rekening.setBalance(jumlah.add(request.getAmount()));
    }

    @Transactional
    public void withdraw(ModifyRekening request) {
        Rekening rekening = rekeningRepository.findByRekId(request.getRekId())
            .orElseThrow(() -> new UsernameNotFoundException(request.getRekId()));
        
        BigDecimal jumlah = rekening.getBalance();
        if (jumlah.compareTo(request.getAmount()) == 1) {
            jumlah = jumlah.subtract(request.getAmount());
        } else {
            jumlah = BigDecimal.ZERO;
        }

        rekening.setBalance(jumlah);
    }
}
