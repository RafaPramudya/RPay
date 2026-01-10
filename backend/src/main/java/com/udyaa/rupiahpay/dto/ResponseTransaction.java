package com.udyaa.rupiahpay.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransaction {
    private Long id;
    private String rekeningId;
    private String type;
    private BigDecimal before;
    private BigDecimal after;

    private ResponseTransfer transfer;
}
