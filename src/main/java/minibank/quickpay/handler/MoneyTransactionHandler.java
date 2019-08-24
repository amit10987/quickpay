package minibank.quickpay.handler;

import minibank.quickpay.domain.service.MoneyTransactionService;
import minibank.quickpay.dto.*;
import minibank.quickpay.exception.AccountNotFoundException;
import minibank.quickpay.exception.InsufficientFund;
import minibank.quickpay.util.JsonUtil;
import spark.Request;
import spark.Response;

import static spark.Spark.exception;
import static spark.Spark.post;

public class MoneyTransactionHandler {

    private MoneyTransactionService moneyTransactionService;

    public MoneyTransactionHandler(MoneyTransactionService moneyTransactionService) {
        this.moneyTransactionService = moneyTransactionService;
    }

    public String transfer(Request req, Response res) {
        res.type("application/json");
        MoneyTransferRequest moneyTransferRequest = JsonUtil.deserialize(req.body(), MoneyTransferRequest.class);
        moneyTransactionService.transfer(moneyTransferRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyTransferResponse("Money successfully transferred."));
    }

    public String deposit(Request req, Response res) {
        res.type("application/json");
        MoneyDepositRequest moneyDepositRequest = JsonUtil.deserialize(req.body(), MoneyDepositRequest.class);
        moneyTransactionService.deposit(moneyDepositRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyDepositResponse("Money successfully deposited."));
    }

    public String withdraw(Request req, Response res) {
        res.type("application/json");
        MoneyWithdrawRequest moneyWithdrawRequest = JsonUtil.deserialize(req.body(), MoneyWithdrawRequest.class);
        moneyTransactionService.withdraw(moneyWithdrawRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyWithdrawResponse("Money successfully withdraw."));
    }

    public void setupEndpoints() {
        post("/transaction/transfer", this::transfer);
        post("/transaction/deposit", this::deposit);
        post("/transaction/withdraw", this::withdraw);
        exception(InsufficientFund.class, QuickPayExceptionHandler::process);
        exception(AccountNotFoundException.class, QuickPayExceptionHandler::process);
    }
}
