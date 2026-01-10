package com.udyaa.rupiahpay.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RestrictedResponseRekening {
    private String rekId;
    private BigDecimal balance;
    private String currency;
}
