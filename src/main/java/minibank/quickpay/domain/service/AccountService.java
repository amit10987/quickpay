package minibank.quickpay.domain.service;

import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.CreateAccountRequest;

import java.util.List;

/**
 * Account service, all the behaviour are stateless in this service.
 * It handles operations like managing the account
 *
 */
public interface AccountService {
    /**
     * This method will create an account for a valid request
     * and returns the valid account number
     *
     * @param req {@link CreateAccountRequest}
     * @return account number
     */
    Long createAccount(CreateAccountRequest req);

    /**
     * @return List of account
     */
    List<Account> getAllAccounts();

    /**
     * Return account entity based on account number
     *
     * @param accountNumber
     * @return Account
     */
    Account getAccount(Long accountNumber);
}
