package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.CreateAccountRequest;

import java.util.List;

public interface AccountService {
    Long createAccount(CreateAccountRequest req);

    List<Account> getAllAccounts();
}
