package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.dto.MoneyDepositRequest;
import com.minibank.quickpay.dto.MoneyTransferRequest;
import com.minibank.quickpay.dto.MoneyWithdrawRequest;

public interface MoneyTransactionService {
    void transfer(MoneyTransferRequest moneyTransferRequest);

    void deposit(MoneyDepositRequest moneyDepositRequest);

    void withdraw(MoneyWithdrawRequest moneyWithdrawRequest);
}
