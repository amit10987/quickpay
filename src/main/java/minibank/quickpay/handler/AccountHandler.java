package minibank.quickpay.handler;

import minibank.quickpay.domain.Account;
import minibank.quickpay.domain.service.AccountService;
import minibank.quickpay.dto.CreateAccountRequest;
import minibank.quickpay.dto.CreateAccountResponse;
import minibank.quickpay.util.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class AccountHandler {

    private AccountService accountService;

    public AccountHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public String createAccount(Request req, Response res) {
        res.type("application/json");
        CreateAccountRequest createAccountRequest = JsonUtil.deserialize(req.body(), CreateAccountRequest.class);
        Long accountNumber = accountService.createAccount(createAccountRequest);
        res.status(201);
        return JsonUtil.serialize(new CreateAccountResponse(accountNumber));
    }

    public String getAllAccounts(Request req, Response res) {
        res.type("application/json");
        List<Account> accounts = accountService.getAllAccounts();
        res.status(200);
        return JsonUtil.serialize(accounts);
    }

    public void setupEndpoints() {
        post("/accounts", this::createAccount);
        get("/accounts", this::getAllAccounts);
    }
}
