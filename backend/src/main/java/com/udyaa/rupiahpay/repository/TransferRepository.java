package com.udyaa.rupiahpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udyaa.rupiahpay.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
