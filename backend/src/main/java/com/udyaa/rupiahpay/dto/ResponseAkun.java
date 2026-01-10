package com.udyaa.rupiahpay.dto;

import java.util.Date;
import java.util.UUID;

import com.udyaa.rupiahpay.enums.AkunRole;

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
public class ResponseAkun {
    private UUID uuid;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private Date createdAt;
    private AkunRole role;
    private RestrictedResponseRekening balance;
}