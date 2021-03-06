package minibank.quickpay.domain;

import minibank.quickpay.exception.InsufficientFund;
import minibank.quickpay.util.QuickPayMessages;

import java.math.BigDecimal;

/**
 * Account class expose most basic feature related to account like credit and debit
 *
 * This class is thread safe
 *
 */
public final class Account {

    private BigDecimal balance;
    private String userName;
    private Long accountNumber;

    public Account(BigDecimal balance, String userName, Long accountNumber) {
        validateMandatoryFields(balance, userName, accountNumber);
        this.balance = balance;
        this.userName = userName;
        this.accountNumber = accountNumber;
    }

    public void credit(BigDecimal amount) {
        if (null == amount || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(QuickPayMessages.INVALID_CREDIT_AMOUNT);
        }
        synchronized (this) {
            this.balance = this.balance.add(amount);
        }
    }

    public void debit(BigDecimal amount) {
        if (null == amount || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(QuickPayMessages.INVALID_DEBIT_AMOUNT);
        }
        synchronized (this) {
            BigDecimal newBalance = this.balance.subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFund();
            }
            this.balance = newBalance;
        }
    }

    private void validateMandatoryFields(BigDecimal balance, String userName, Long accountNumber) {
        if (null == balance || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(QuickPayMessages.INVALID_OPENING_BALANCE);
        }

        if (null == userName || userName.isEmpty()) {
            throw new IllegalArgumentException(QuickPayMessages.INVALID_USER_NAME);
        }

        if (null == accountNumber || accountNumber.toString().length() < 10) {
            throw new IllegalArgumentException(QuickPayMessages.INVALID_ACCOUNT_NUMBER);
        }
    }

    public synchronized BigDecimal getBalance() {
        return balance.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getUserName() {
        return userName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

}
