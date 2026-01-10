package com.udyaa.rupiahpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.udyaa.rupiahpay.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    @Query("SELECT t FROM Transaction t WHERE t.rekening.id = :id")
    List<Transaction> findAllSourcebyRekId(@Param("id") Long id);
}
