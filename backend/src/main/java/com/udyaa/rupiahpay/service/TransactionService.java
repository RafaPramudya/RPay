package com.udyaa.rupiahpay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.udyaa.rupiahpay.dto.RequestTransfer;
import com.udyaa.rupiahpay.dto.ResponseTransaction;
import com.udyaa.rupiahpay.dto.ResponseTransfer;
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

        Transfer transfer = Transfer.builder()
            .sender(null)
            .receiver(rekening)
            .initiatedAt(new Date())
            .amount(request.getAmount())
            .notes("Deposit")
            .status(TransferStatus.COMPLETED)
            .build();
        Transaction transaction = Transaction.builder()
            .rekening(rekening)
            .type(TransactionType.CREDIT)
            .beforeBalance(jumlah)
            .afterBalance(rekening.getBalance())
            .transfer(transfer)
            .build();

        transferRepository.save(transfer);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(RequestTransfer request) {
        Rekening rekening = rekeningRepository.findByRekId(request.getRekId())
            .orElseThrow(() -> new UsernameNotFoundException(request.getRekId()));
        
        BigDecimal jumlah = rekening.getBalance();
        BigDecimal before = jumlah;
        if (jumlah.compareTo(request.getAmount()) == 1) {
            jumlah = jumlah.subtract(request.getAmount());
        } else {
            request.setAmount(jumlah);
            jumlah = BigDecimal.ZERO;
        }

        rekening.setBalance(jumlah);

        Transfer transfer = Transfer.builder()
            .sender(rekening)
            .receiver(null)
            .initiatedAt(new Date())
            .amount(request.getAmount())
            .notes("Withdraw")
            .status(TransferStatus.COMPLETED)
            .build();
        Transaction transaction = Transaction.builder()
            .rekening(rekening)
            .type(TransactionType.DEBIT)
            .beforeBalance(before)
            .afterBalance(jumlah)
            .transfer(transfer)
            .build();

        transferRepository.save(transfer);
        transactionRepository.save(transaction);
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

        Transaction senderTransaction = Transaction.builder()
            .rekening(sender)
            .type(TransactionType.DEBIT)
            .beforeBalance(sender.getBalance())
            .afterBalance(newSenderBalance)
            .transfer(transfer)
            .build();
        Transaction receiverTransaction = Transaction.builder()
            .rekening(receiver)
            .type(TransactionType.CREDIT)
            .beforeBalance(receiver.getBalance())
            .afterBalance(newReceiverBalance)
            .transfer(transfer)
            .build();
        
        sender.setBalance(newSenderBalance);
        receiver.setBalance(newReceiverBalance);

        transferRepository.save(transfer);
        transactionRepository.saveAll(List.of(senderTransaction, receiverTransaction));
    }

    public List<ResponseTransaction> getTransactionHistory(String email) {
        Rekening rekening = akunService.getAccountByEmail(email).getBalance();
        List<ResponseTransaction> transactions = transactionRepository.findAllRekeningbyId(rekening.getId())
            .stream()
            .map(t -> {
                Transfer transfer = t.getTransfer();
                Rekening sender = transfer.getSender();
                Rekening receiver = transfer.getReceiver();

                ResponseTransaction responseTransaction = ResponseTransaction.builder()
                    .id(t.getId())
                    .rekeningId(t.getRekening().getRekId())
                    .type(t.getType().name())
                    .before(t.getBeforeBalance())
                    .after(t.getAfterBalance())
                    .transfer( ResponseTransfer.builder()
                        .id(transfer.getId())
                        .amount(transfer.getAmount())
                        .senderId(sender != null ? sender.getRekId() : null)
                        .receiverId(receiver != null ? receiver.getRekId() : null)
                        .initiatedAt(transfer.getInitiatedAt())
                        .status(transfer.getStatus().name())
                        .notes(transfer.getNotes())
                        .build()
                    )
                    .build();
                
                return responseTransaction;
            })
            .toList()
            .reversed();

        return transactions;
    }
}
