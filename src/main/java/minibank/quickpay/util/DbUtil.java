package minibank.quickpay.util;

import minibank.quickpay.QuickPayDataSource;
import minibank.quickpay.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.math.BigDecimal;

public final class DbUtil {

    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static final String INITIALIZATION_SCRIPT = "CREATE TABLE account (accountNumber BIGINT NOT NULL, userName VARCHAR(50) NOT NULL, balance DECIMAL(10,2) NOT NULL, PRIMARY KEY (accountNumber))";
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO ACCOUNT(accountNumber, userName, balance) values (:accountNumber, :userName, :balance)";

    private DbUtil() {
    }

    public static void initialize() {
        try (Connection connection = QuickPayDataSource.getSql2o().open();
             Query query = connection.createQuery(INITIALIZATION_SCRIPT)) {
            query.executeUpdate();
        }

        logger.info("Account table created successfully");

        try (Connection connection = QuickPayDataSource.getSql2o().open();
             Query query = connection.createQuery(INSERT_ACCOUNT_SQL)) {

            Account account1 = new Account(new BigDecimal(500), "Amit", 6642159765L);
            Account account2 = new Account(new BigDecimal(500), "Anil", 6642159766L);

            query.bind(account1).addToBatch();
            query.bind(account2).addToBatch();

            query.executeBatch();
        }

        logger.info("Account table populated with two default accounts");
    }
}
