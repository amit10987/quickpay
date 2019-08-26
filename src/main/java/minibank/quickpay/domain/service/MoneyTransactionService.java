package minibank.quickpay.domain.service;

import minibank.quickpay.dto.MoneyDepositRequest;
import minibank.quickpay.dto.MoneyTransferRequest;
import minibank.quickpay.dto.MoneyWithdrawRequest;

/**
 * Handles money transactions operations
 */
public interface MoneyTransactionService {
    /**
     * Transfer amount from one account to another
     *
     * @param moneyTransferRequest
     */
    void transfer(MoneyTransferRequest moneyTransferRequest);

    /**
     * Deposit money
     *
     * @param moneyDepositRequest
     */
    void deposit(MoneyDepositRequest moneyDepositRequest);

    /**
     * Withdraw money
     *
     * @param moneyWithdrawRequest
     */
    void withdraw(MoneyWithdrawRequest moneyWithdrawRequest);
}
