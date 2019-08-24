package minibank.quickpay.domain.service;

import minibank.quickpay.dto.MoneyDepositRequest;
import minibank.quickpay.dto.MoneyTransferRequest;
import minibank.quickpay.dto.MoneyWithdrawRequest;

public interface MoneyTransactionService {
    void transfer(MoneyTransferRequest moneyTransferRequest);

    void deposit(MoneyDepositRequest moneyDepositRequest);

    void withdraw(MoneyWithdrawRequest moneyWithdrawRequest);
}
