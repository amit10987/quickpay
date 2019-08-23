package com.minibank.quickpay.infrastructure;

import com.minibank.quickpay.QuickPayDataSource;
import com.minibank.quickpay.domain.Account;
import org.sql2o.Connection;
import org.sql2o.StatementRunnable;

import java.util.List;


public class AccountRepository {

    private static final String CREATE_ACCOUNT_SQL = "INSERT INTO ACCOUNT(accountNumber, userName, balance) values (:accountNumber, :userName, :balance)";
    private static final String GET_ALL_ACCOUNTS_SQL = "SELECT * FROM ACCOUNT";
    private static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL = "SELECT * FROM ACCOUNT WHERE accountNumber = :accountNumber";
    private static final String UPDATE_ACCOUNT_SET_BALANCE_SQL = "UPDATE ACCOUNT SET balance = :balance WHERE accountNumber = :accountNumber";

    public void save(Account account) {
        try (Connection connection = QuickPayDataSource.getSql2o().open()) {
            connection.createQuery(CREATE_ACCOUNT_SQL).bind(account).executeUpdate();
        }
    }

    public List<Account> findAll() {
        try (Connection connection = QuickPayDataSource.getSql2o().open()) {
            return connection.createQuery(GET_ALL_ACCOUNTS_SQL).executeAndFetch(Account.class);
        }
    }

    public Account findByAccountNumber(Long accountNumber) {
        try (Connection connection = QuickPayDataSource.getSql2o().open()) {
            return connection.createQuery(GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL).addParameter("accountNumber", accountNumber).executeAndFetchFirst(Account.class);
        }
    }

    public Account findByAccountNumber(Long accountNumber, Connection connection) {
        return connection.createQuery(GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL).addParameter("accountNumber", accountNumber).executeAndFetchFirst(Account.class);
    }

    public void update(Account account, Connection connection) {
        connection.createQuery(UPDATE_ACCOUNT_SET_BALANCE_SQL).bind(account).executeUpdate();
    }
}
