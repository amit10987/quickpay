package com.minibank.quickpay.domain;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private String userName;
    private Long accountNumber;

    public Account(BigDecimal balance, String userName, Long accountNumber) {
        this.balance = balance;
        this.userName = userName;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getUserName() {
        return userName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }


}
