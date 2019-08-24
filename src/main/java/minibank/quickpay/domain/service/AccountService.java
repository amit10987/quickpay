package minibank.quickpay.domain.service;

import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.CreateAccountRequest;

import java.util.List;

public interface AccountService {
    Long createAccount(CreateAccountRequest req);

    List<Account> getAllAccounts();
}
