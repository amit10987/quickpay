package com.minibank.quickpay.infrastructure;

import com.minibank.quickpay.domain.Account;
import org.sql2o.Connection;

import java.util.List;

public interface AccountRepository {

    void save(Account account);

    List<Account> findAll();

    Account findByAccountNumber(Long accountNumber, Connection connection);

    void update(Account account, Connection connection);
}
