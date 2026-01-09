package com.udyaa.rupiahpay.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class SaldoAkun {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 

    @Builder.Default
    @Column(precision=19, scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Column(length=3)
    private String currency = "IDR";
}
