package com.udyaa.rupiahpay.dto;

import java.math.BigDecimal;
import java.util.Date;

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
public class ResponseTransfer {
    private Long id;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private Date initiatedAt;
    private String status;
    private String notes;
}
