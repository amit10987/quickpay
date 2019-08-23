package com.minibank.quickpay.handler;

import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.CreateAccountRequest;
import com.minibank.quickpay.dto.CreateAccountResponse;
import com.minibank.quickpay.domain.service.AccountService;
import com.minibank.quickpay.util.JsonUtil;
import spark.Route;

import java.util.List;

public class AccountHandler {

       public static Route createAccount = (req, res) -> {
        res.type("application/json");
        AccountService accountService = new AccountService();
        CreateAccountRequest createAccountRequest = JsonUtil.deserialize(req.body(), CreateAccountRequest.class);
        Long accountNumber = accountService.createAccount(createAccountRequest);
        res.status(201);
        return JsonUtil.serialize(new CreateAccountResponse(accountNumber));
    };

    public static Route getAllAccounts = (req, res) -> {
        res.type("application/json");
        AccountService accountService = new AccountService();
        List<Account> accounts = accountService.getAllAccounts();
        res.status(200);
        return JsonUtil.serialize(accounts);
    };

}
