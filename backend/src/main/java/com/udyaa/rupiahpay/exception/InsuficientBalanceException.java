package com.udyaa.rupiahpay.exception;

public class InsuficientBalanceException extends RuntimeException {
    public InsuficientBalanceException() {
        super("Saldo tidak mencukupi");
    }
}
