package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.CreateAccountRequest;
import com.minibank.quickpay.infrastructure.AccountRepository;
import com.minibank.quickpay.infrastructure.AccountRepositoryImpl;

import java.util.List;

public class AccountServiceImpl implements AccountService{

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    @Override
    public Long createAccount(CreateAccountRequest req) {
        Long accountNumber = generateAccountNumber();
        Account account = new Account(req.getOpeningBalance(), req.getUserName(), accountNumber);
        accountRepository.save(account);
        return accountNumber;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private Long generateAccountNumber() {
        return System.currentTimeMillis() % 10000000000L;
    }
}
