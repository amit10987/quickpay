package com.minibank.quickpay.util;

import com.minibank.quickpay.QuickPayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;

public class DbUtil {

    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static final String INITIALIZATION_SCRIPT ="CREATE TABLE account (account_number BIGINT NOT NULL, user_name VARCHAR(50) NOT NULL, balance DECIMAL(10,2) NOT NULL, PRIMARY KEY (account_number))";

    private DbUtil() {
    }

    public static void initialize() {
        Connection connection = QuickPayDataSource.getSql2o().open();
        try(Query query = connection.createQuery(INITIALIZATION_SCRIPT)){
            query.executeUpdate();
        }
    }
}
