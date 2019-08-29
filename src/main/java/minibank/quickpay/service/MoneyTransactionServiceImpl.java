package minibank.quickpay.service;

import minibank.quickpay.QuickPayDataSource;
import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.MoneyDepositRequest;
import minibank.quickpay.dto.MoneyTransferRequest;
import minibank.quickpay.dto.MoneyWithdrawRequest;
import minibank.quickpay.infrastructure.AccountRepository;
import org.sql2o.Connection;

public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    private AccountRepository accountRepository;

    public MoneyTransactionServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * logic inside this method runs in TRANSACTION_READ_COMMITTED isolation level
     *
     * @param moneyTransferRequest
     */
    @Override
    public void transfer(MoneyTransferRequest moneyTransferRequest) {
        //by default Sql2o begin transaction with READ_COMMITTED isolation level
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
