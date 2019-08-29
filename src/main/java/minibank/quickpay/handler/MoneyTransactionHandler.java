package minibank.quickpay.handler;

import minibank.quickpay.service.MoneyTransactionService;
import minibank.quickpay.dto.*;
import minibank.quickpay.exception.AccountNotFoundException;
import minibank.quickpay.exception.InsufficientFund;
import minibank.quickpay.util.JsonUtil;
import minibank.quickpay.util.QuickPayEndPoint;
import minibank.quickpay.util.QuickPayMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.exception;
import static spark.Spark.post;

public class MoneyTransactionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransactionHandler.class);

    private static final String APPLICATION_JSON = "application/json";

    private MoneyTransactionService moneyTransactionService;

    public MoneyTransactionHandler(MoneyTransactionService moneyTransactionService) {
        this.moneyTransactionService = moneyTransactionService;
    }

    public String transfer(Request req, Response res) {
        logger.info("Got request to transfer the money: {} ", req.body());
        res.type(APPLICATION_JSON);
        MoneyTransferRequest moneyTransferRequest = JsonUtil.deserialize(req.body(), MoneyTransferRequest.class);
        moneyTransactionService.transfer(moneyTransferRequest);
        res.status(200);
        String transferRes = JsonUtil.serialize(new MoneyTransferResponse(QuickPayMessages.SUCCESSFUL_TRANSFER));
        logger.info("Money successfully transfer: {}", transferRes);
        return transferRes;
    }

    public String deposit(Request req, Response res) {
        logger.info("Got request to deposit the money: {} ", req.body());
        res.type(APPLICATION_JSON);
        MoneyDepositRequest moneyDepositRequest = JsonUtil.deserialize(req.body(), MoneyDepositRequest.class);
        moneyTransactionService.deposit(moneyDepositRequest);
        res.status(200);
        String depositRes = JsonUtil.serialize(new MoneyDepositResponse(QuickPayMessages.SUCCESSFUL_DEPOSIT));
        logger.info("Money Successfully deposit: {}", depositRes);
        return depositRes;
    }

    public String withdraw(Request req, Response res) {
        logger.info("Got request to withdraw the money: {} ", req.body());
        res.type(APPLICATION_JSON);
        MoneyWithdrawRequest moneyWithdrawRequest = JsonUtil.deserialize(req.body(), MoneyWithdrawRequest.class);
        moneyTransactionService.withdraw(moneyWithdrawRequest);
        res.status(200);
        String withdrawRes = JsonUtil.serialize(new MoneyWithdrawResponse(QuickPayMessages.SUCCESSFUL_WITHDRAW));
        logger.info("Money Successfully withdraw: {}", withdrawRes);
        return withdrawRes;
    }

    public void setupEndpoints() {
        post(QuickPayEndPoint.MoneyTransaction.TRANSFER, this::transfer);
        post(QuickPayEndPoint.MoneyTransaction.DEPOSIT, this::deposit);
        post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, this::withdraw);
        exception(InsufficientFund.class, QuickPayExceptionHandler::process);
        exception(AccountNotFoundException.class, QuickPayExceptionHandler::process);
    }
}
