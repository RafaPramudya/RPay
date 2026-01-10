package com.udyaa.rupiahpay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.RequestTransfer;
import com.udyaa.rupiahpay.entity.Rekening;
import com.udyaa.rupiahpay.entity.Transaction;
import com.udyaa.rupiahpay.entity.Transfer;
import com.udyaa.rupiahpay.enums.TransactionType;
import com.udyaa.rupiahpay.enums.TransferStatus;
import com.udyaa.rupiahpay.exception.InsuficientBalanceException;
import com.udyaa.rupiahpay.repository.RekeningRepository;
import com.udyaa.rupiahpay.repository.TransactionRepository;
import com.udyaa.rupiahpay.repository.TransferRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {
    private final AkunService akunService;

    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;
    private final RekeningRepository rekeningRepository;

    @Transactional
    public void deposit(RequestTransfer request) {
        Rekening rekening = rekeningRepository.findByRekId(request.getRekId())
            .orElseThrow(() -> new UsernameNotFoundException(request.getRekId()));
        
        BigDecimal jumlah = rekening.getBalance();
        rekening.setBalance(jumlah.add(request.getAmount()));
    }

    @Transactional
    public void withdraw(RequestTransfer request) {
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

    @Transactional
    public void transfer(RequestTransfer requestTransfer, String email) {
        Rekening sender = akunService.getAccountByEmail(email).getBalance();
        Rekening receiver = rekeningRepository.findByRekId(requestTransfer.getRekId())
            .orElseThrow(() -> new UsernameNotFoundException(requestTransfer.getRekId()));

        Transfer transfer = Transfer.builder()
            .sender(sender)
            .receiver(receiver)
            .initiatedAt(new Date())
            .amount(requestTransfer.getAmount())
            .notes(requestTransfer.getNotes())
            .status(TransferStatus.COMPLETED)
            .build();

        // Transaction Happens
        if (sender.getBalance().compareTo(requestTransfer.getAmount()) == -1) {
            transfer.setStatus(TransferStatus.FAILED);
            transfer.setNotes("Saldo tidak mencukupi");
            transferRepository.save(transfer);

            throw new InsuficientBalanceException();
        }

        BigDecimal newSenderBalance = sender.getBalance().subtract(requestTransfer.getAmount());
        BigDecimal newReceiverBalance = receiver.getBalance().add(requestTransfer.getAmount());

        sender.setBalance(newSenderBalance);
        receiver.setBalance(newReceiverBalance);

        Transaction senderTransaction = Transaction.builder()
            .rekening(sender)
            .transfer(transfer)
            .type(TransactionType.DEBIT)
            .build();
        Transaction receiverTransaction = Transaction.builder()
            .rekening(receiver)
            .transfer(transfer)
            .type(TransactionType.CREDIT)
            .build();

        transferRepository.save(transfer);
        transactionRepository.saveAll(List.of(senderTransaction, receiverTransaction));
    }
}
