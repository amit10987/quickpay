package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.QuickPayDataSource;
import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.MoneyDepositRequest;
import com.minibank.quickpay.dto.MoneyTransferRequest;
import com.minibank.quickpay.dto.MoneyWithdrawRequest;
import com.minibank.quickpay.infrastructure.AccountRepository;

public class MoneyTransactionService {

    public void transfer(MoneyTransferRequest moneyTransferRequest){
        AccountRepository accountRepository = new AccountRepository();
        QuickPayDataSource.getSql2o().runInTransaction((con, obj) -> {
            Account fromAccount = accountRepository.findByAccountNumber(moneyTransferRequest.getFromAccountNumber(), con);
            Account toAccount = accountRepository.findByAccountNumber(moneyTransferRequest.getToAccountNumber(), con);
            fromAccount.debit(moneyTransferRequest.getTransferAmount());
            toAccount.credit(moneyTransferRequest.getTransferAmount());
            accountRepository.update(fromAccount, con);
            accountRepository.update(toAccount,con);
        });
    }

    public void deposit(MoneyDepositRequest moneyDepositRequest){
        AccountRepository accountRepository = new AccountRepository();
        QuickPayDataSource.getSql2o().runInTransaction((con, obj) -> {
            Account account = accountRepository.findByAccountNumber(moneyDepositRequest.getAccountNumber(), con);
            account.credit(moneyDepositRequest.getAmount());
            accountRepository.update(account, con);
        });
    }

    public void withdraw(MoneyWithdrawRequest moneyWithdrawRequest){
        AccountRepository accountRepository = new AccountRepository();
        QuickPayDataSource.getSql2o().runInTransaction((con, obj) -> {
            Account account = accountRepository.findByAccountNumber(moneyWithdrawRequest.getAccountNumber(), con);
            account.debit(moneyWithdrawRequest.getAmount());
            accountRepository.update(account, con);
        });
    }
}
