package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.QuickPayDataSource;
import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.MoneyDepositRequest;
import com.minibank.quickpay.dto.MoneyTransferRequest;
import com.minibank.quickpay.dto.MoneyWithdrawRequest;
import com.minibank.quickpay.infrastructure.AccountRepository;
import org.sql2o.Connection;

public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    private AccountRepository accountRepository;

    public MoneyTransactionServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transfer(MoneyTransferRequest moneyTransferRequest) {
        try (Connection con = QuickPayDataSource.getSql2o().beginTransaction()) {
            Account fromAccount = accountRepository.findByAccountNumber(moneyTransferRequest.getFromAccountNumber(), con);
            Account toAccount = accountRepository.findByAccountNumber(moneyTransferRequest.getToAccountNumber(), con);
            fromAccount.debit(moneyTransferRequest.getTransferAmount());
            toAccount.credit(moneyTransferRequest.getTransferAmount());
            accountRepository.update(fromAccount, con);
            accountRepository.update(toAccount, con);
            con.commit();
        }
    }

    @Override
    public void deposit(MoneyDepositRequest moneyDepositRequest) {
        try (Connection con = QuickPayDataSource.getSql2o().beginTransaction()) {
            Account account = accountRepository.findByAccountNumber(moneyDepositRequest.getAccountNumber(), con);
            account.credit(moneyDepositRequest.getAmount());
            accountRepository.update(account, con);
            con.commit();
        }
    }

    @Override
    public void withdraw(MoneyWithdrawRequest moneyWithdrawRequest) {
        try (Connection con = QuickPayDataSource.getSql2o().beginTransaction()) {
            Account account = accountRepository.findByAccountNumber(moneyWithdrawRequest.getAccountNumber(), con);
            account.debit(moneyWithdrawRequest.getAmount());
            accountRepository.update(account, con);
            con.commit();
        }
    }
}
