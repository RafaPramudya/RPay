package com.udyaa.rupiahpay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udyaa.rupiahpay.entity.Rekening;

public interface RekeningRepository extends JpaRepository<Rekening, Long> {
    Optional<Rekening> findByRekId(String rekId);
}
