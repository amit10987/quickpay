package minibank.quickpay.infrastructure;

import minibank.quickpay.QuickPayDataSource;
import minibank.quickpay.domain.Account;
import minibank.quickpay.exception.AccountNotFoundException;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.util.List;


public class AccountRepositoryImpl implements AccountRepository {

    private static final String CREATE_ACCOUNT_SQL = "INSERT INTO ACCOUNT(accountNumber, userName, balance) values (:accountNumber, :userName, :balance)";
    private static final String GET_ALL_ACCOUNTS_SQL = "SELECT * FROM ACCOUNT";
    private static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL = "SELECT * FROM ACCOUNT WHERE accountNumber = :accountNumber";
    private static final String UPDATE_ACCOUNT_SET_BALANCE_SQL = "UPDATE ACCOUNT SET balance = :balance WHERE accountNumber = :accountNumber";

    @Override
    public void save(Account account) {
        try (Connection connection = QuickPayDataSource.getSql2o().open();
             Query query = connection.createQuery(CREATE_ACCOUNT_SQL)) {
            query.bind(account).executeUpdate();
        }
    }

    @Override
    public List<Account> findAll() {
        try (Connection connection = QuickPayDataSource.getSql2o().open();
             Query query = connection.createQuery(GET_ALL_ACCOUNTS_SQL)) {
            return query.executeAndFetch(Account.class);
        }
    }

    @Override
    public Account findByAccountNumber(Long accountNumber, Connection connection) {
        try (Query query = connection.createQuery(GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL)) {
            Account account = query.addParameter("accountNumber", accountNumber).executeAndFetchFirst(Account.class);
            if (null == account) {
                throw new AccountNotFoundException();
            }
            return account;
        }
    }

    @Override
    public void update(Account account, Connection connection) {
        try (Query query = connection.createQuery(UPDATE_ACCOUNT_SET_BALANCE_SQL)) {
            query.bind(account).executeUpdate();
        }
    }

    @Override
    public Account findByAccountNumber(Long accountNumber) {
        Account account;
        try (Connection connection = QuickPayDataSource.getSql2o().open();
             Query query = connection.createQuery(GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL)) {
            account = query.addParameter("accountNumber", accountNumber).executeAndFetchFirst(Account.class);
        }

        if (null == account) {
            throw new AccountNotFoundException();
        }
        return account;
    }
}
