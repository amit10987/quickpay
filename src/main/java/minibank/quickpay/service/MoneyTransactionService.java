package minibank.quickpay.service;

import minibank.quickpay.dto.MoneyDepositRequest;
import minibank.quickpay.dto.MoneyTransferRequest;
import minibank.quickpay.dto.MoneyWithdrawRequest;

/**
 * Handles money transactions operations
 */
public interface MoneyTransactionService {
    /**
     * Transfer amount from one account to another
     * this method should run inside isolation level READ_COMMITTED
     *
     * @param moneyTransferRequest
     */
    void transfer(MoneyTransferRequest moneyTransferRequest);

    /**
     * Deposit money
     * this method should run inside isolation level READ_COMMITTED
     *
     * @param moneyDepositRequest
     */
    void deposit(MoneyDepositRequest moneyDepositRequest);

    /**
     * Withdraw money
     * this method should run inside isolation level READ_COMMITTED
     *
     * @param moneyWithdrawRequest
     */
    void withdraw(MoneyWithdrawRequest moneyWithdrawRequest);
}
