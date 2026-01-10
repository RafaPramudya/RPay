package com.udyaa.rupiahpay.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
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
public class Rekening {
    @Id
    @GeneratedValue(generator="rekening-gen")
    @SequenceGenerator(
        name="rekening-gen",
        sequenceName="rekening_gen",
        allocationSize= 50
    )
    private Long id;

    @Column(unique=true, length=32, nullable=false)
    private String rekId; 

    @Builder.Default
    @Column(precision=19, scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Column(length=3)
    private String currency = "IDR";

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="uuid")
    private Akun owner;

    @PrePersist
    public void generateRekeningId() {
        this.rekId = "REK" + String.format("%016d", id);  
    }
}
