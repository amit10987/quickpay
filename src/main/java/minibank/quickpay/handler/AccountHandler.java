package minibank.quickpay.handler;

import minibank.quickpay.domain.Account;
import minibank.quickpay.domain.service.AccountService;
import minibank.quickpay.dto.CreateAccountRequest;
import minibank.quickpay.dto.CreateAccountResponse;
import minibank.quickpay.util.JsonUtil;
import minibank.quickpay.util.QuickPayEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Account endpoints
 */
public class AccountHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountHandler.class);

    private static final String APPLICATION_JSON = "application/json";

    private AccountService accountService;

    public AccountHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public String createAccount(Request req, Response res) {
        logger.info("Got request to create new account: {} ", req.body());
        res.type(APPLICATION_JSON);
        CreateAccountRequest createAccountRequest = JsonUtil.deserialize(req.body(), CreateAccountRequest.class);
        Long accountNumber = accountService.createAccount(createAccountRequest);
        res.status(201);
        String createAccountResponse = JsonUtil.serialize(new CreateAccountResponse(accountNumber));
        logger.info("Account successfully created: {}", createAccountResponse);
        return createAccountResponse;
    }

    public String getAllAccounts(Request req, Response res) {
        logger.info("Got request to get all accounts: {}", req.body());
        res.type(APPLICATION_JSON);
        List<Account> accounts = accountService.getAllAccounts();
        res.status(200);
        String accountsRes = JsonUtil.serialize(accounts);
        logger.info("List of accounts: {}", accountsRes);
        return accountsRes;
    }

    public String getAccount(Request req, Response res) {
        logger.info("Got request to get account based on account number: {}", req.body());
        res.type(APPLICATION_JSON);
        Account account = accountService.getAccount(Long.valueOf(req.params(":accountNumber")));
        res.status(200);
        String accountRes = JsonUtil.serialize(account);
        logger.info("Account by account number: {}", accountRes);
        return JsonUtil.serialize(account);
    }

    public void setupEndpoints() {
        post(QuickPayEndPoint.Account.CREATE_ACCOUNT, this::createAccount);
        get(QuickPayEndPoint.Account.GET_ALL_ACCOUNTS, this::getAllAccounts);
        get(QuickPayEndPoint.Account.GET_ACCOUNT_BY_ACCOUNT_NUMBER, this::getAccount);
    }
}
