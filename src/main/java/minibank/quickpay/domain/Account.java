package minibank.quickpay.domain;

import minibank.quickpay.exception.InsufficientFund;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private String userName;
    private Long accountNumber;

    public Account(BigDecimal balance, String userName, Long accountNumber) {
        if (null == balance || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Opening balance must not be negative.");
        }

        if (null == userName || userName.isEmpty()) {
            throw new IllegalArgumentException("User Name must not be empty.");
        }

        if(null == accountNumber){
            throw new IllegalArgumentException("Account number must not be empty.");
        }

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
        if (null == amount || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Not a valid amount to credit");
        }
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        if (null == amount || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Not a valid amount to debit");
        }
        BigDecimal newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFund();
        }
        this.balance = newBalance;
    }
}
