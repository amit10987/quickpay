package minibank.quickpay.infrastructure;

import minibank.quickpay.domain.Account;
import org.sql2o.Connection;

import java.util.List;

public interface AccountRepository {

    void save(Account account);

    List<Account> findAll();

    Account findByAccountNumber(Long accountNumber, Connection connection);

    Account findByAccountNumber(Long accountNumber);

    void update(Account account, Connection connection);
}
