package com.minibank.quickpay.domain.service;

import com.minibank.quickpay.QuickPayDataSource;
import com.minibank.quickpay.domain.Account;
import com.minibank.quickpay.dto.MoneyTransferRequest;
import com.minibank.quickpay.infrastructure.AccountRepository;

public class MoneyTransferService {

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
}
