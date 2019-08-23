package com.minibank.quickpay.handler;

import com.minibank.quickpay.dto.*;
import com.minibank.quickpay.domain.service.MoneyTransactionService;
import com.minibank.quickpay.util.JsonUtil;
import spark.Route;

public class MoneyTransactionHandler {

    public static Route transfer = (req, res) -> {
        res.type("application/json");
        MoneyTransactionService moneyTransactionService = new MoneyTransactionService();
        MoneyTransferRequest moneyTransferRequest = JsonUtil.deserialize(req.body(), MoneyTransferRequest.class);
        moneyTransactionService.transfer(moneyTransferRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyTransferResponse("Money successfully transferred."));
    };

    public static Route deposit = (req, res) -> {
        res.type("application/json");
        MoneyTransactionService moneyTransactionService = new MoneyTransactionService();
        MoneyDepositRequest moneyDepositRequest = JsonUtil.deserialize(req.body(), MoneyDepositRequest.class);
        moneyTransactionService.deposit(moneyDepositRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyDepositResponse("Money successfully deposited."));
    };

    public static Route withdraw = (req, res) -> {
        res.type("application/json");
        MoneyTransactionService moneyTransactionService = new MoneyTransactionService();
        MoneyWithdrawRequest moneyWithdrawRequest = JsonUtil.deserialize(req.body(), MoneyWithdrawRequest.class);
        moneyTransactionService.withdraw(moneyWithdrawRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyWithdrawResponse("Money successfully withdraw."));
    };
}
