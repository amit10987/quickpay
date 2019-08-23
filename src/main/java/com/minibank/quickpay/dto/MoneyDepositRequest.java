package com.minibank.quickpay.dto;

import java.math.BigDecimal;

public class MoneyDepositRequest {

    private Long accountNumber;
    private BigDecimal amount;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
