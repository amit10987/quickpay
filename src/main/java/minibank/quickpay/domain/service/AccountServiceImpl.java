package minibank.quickpay.domain.service;

import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.CreateAccountRequest;
import minibank.quickpay.infrastructure.AccountRepository;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
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

    @Override
    public Account getAccount(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    private Long generateAccountNumber() {
        return System.currentTimeMillis() % 10000000000L;
    }
}
