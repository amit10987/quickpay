package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.CreateAccountRequest;
import com.minibank.quickpay.dto.CreateAccountResponse;
import com.minibank.quickpay.infrastructure.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AccountService {

    public Long createAccount(CreateAccountRequest req) {
        AccountRepository accountRepository = new AccountRepository();
        Long accountNumber = generateAccountNumber();
        Account account = new Account(req.getOpeningBalance(), req.getUserName(), accountNumber);
        accountRepository.save(account);
        return accountNumber;
    }

    public List<Account> getAllAccounts() {
        AccountRepository accountRepository = new AccountRepository();
        return accountRepository.findAll();
    }

    private Long generateAccountNumber() {
        return System.currentTimeMillis() % 10000000000L;
    }
}
