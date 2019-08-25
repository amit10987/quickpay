package minibank.quickpay.handler;

import minibank.quickpay.domain.service.MoneyTransactionService;
import minibank.quickpay.dto.*;
import minibank.quickpay.exception.AccountNotFoundException;
import minibank.quickpay.exception.InsufficientFund;
import minibank.quickpay.util.JsonUtil;
import minibank.quickpay.util.QuickPayEndPoint;
import minibank.quickpay.util.QuickPayMessages;
import spark.Request;
import spark.Response;

import static spark.Spark.exception;
import static spark.Spark.post;

public class MoneyTransactionHandler {

    private static final String APPLICATION_JSON = "application/json";

    private MoneyTransactionService moneyTransactionService;

    public MoneyTransactionHandler(MoneyTransactionService moneyTransactionService) {
        this.moneyTransactionService = moneyTransactionService;
    }

    public String transfer(Request req, Response res) {
        res.type(APPLICATION_JSON);
        MoneyTransferRequest moneyTransferRequest = JsonUtil.deserialize(req.body(), MoneyTransferRequest.class);
        moneyTransactionService.transfer(moneyTransferRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyTransferResponse(QuickPayMessages.SUCCESSFUL_TRANSFER));
    }

    public String deposit(Request req, Response res) {
        res.type(APPLICATION_JSON);
        MoneyDepositRequest moneyDepositRequest = JsonUtil.deserialize(req.body(), MoneyDepositRequest.class);
        moneyTransactionService.deposit(moneyDepositRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyDepositResponse(QuickPayMessages.SUCCESSFUL_DEPOSIT));
    }

    public String withdraw(Request req, Response res) {
        res.type(APPLICATION_JSON);
        MoneyWithdrawRequest moneyWithdrawRequest = JsonUtil.deserialize(req.body(), MoneyWithdrawRequest.class);
        moneyTransactionService.withdraw(moneyWithdrawRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyWithdrawResponse(QuickPayMessages.SUCCESSFUL_WITHDRAW));
    }

    public void setupEndpoints() {
        post(QuickPayEndPoint.MoneyTransaction.TRANSFER, this::transfer);
        post(QuickPayEndPoint.MoneyTransaction.DEPOSIT, this::deposit);
        post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, this::withdraw);
        exception(InsufficientFund.class, QuickPayExceptionHandler::process);
        exception(AccountNotFoundException.class, QuickPayExceptionHandler::process);
    }
}
