package com.minibank.quickpay;


import com.minibank.quickpay.domain.service.AccountService;
import com.minibank.quickpay.domain.service.AccountServiceImpl;
import com.minibank.quickpay.domain.service.MoneyTransactionService;
import com.minibank.quickpay.domain.service.MoneyTransactionServiceImpl;
import com.minibank.quickpay.handler.AccountHandler;
import com.minibank.quickpay.handler.MoneyTransactionHandler;
import com.minibank.quickpay.infrastructure.AccountRepository;
import com.minibank.quickpay.infrastructure.AccountRepositoryImpl;
import com.minibank.quickpay.util.DbUtil;
import com.minibank.quickpay.handler.QuickPayExceptionHandler;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        DbUtil.initialize();

        AccountRepository repository = new AccountRepositoryImpl();
        setupAccountHandler(repository);
        setupMoneyTransactionHandler(repository);

        notFound(QuickPayExceptionHandler::notFound);
        internalServerError(QuickPayExceptionHandler::internalServerError);
        exception(IllegalArgumentException.class, QuickPayExceptionHandler::process);
    }

    private static void setupMoneyTransactionHandler(AccountRepository accountRepository) {
        MoneyTransactionService moneyTransactionService = new MoneyTransactionServiceImpl(accountRepository);
        MoneyTransactionHandler moneyTransactionHandler = new MoneyTransactionHandler(moneyTransactionService);
        moneyTransactionHandler.setupEndpoints();
    }

    private static void setupAccountHandler(AccountRepository accountRepository) {
        AccountService accountService = new AccountServiceImpl(accountRepository);
        AccountHandler accountHandler = new AccountHandler(accountService);
        accountHandler.setupEndpoints();
    }
}
