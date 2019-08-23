package com.minibank.quickpay.handler;

import com.minibank.quickpay.dto.MoneyTransferRequest;
import com.minibank.quickpay.dto.MoneyTransferResponse;
import com.minibank.quickpay.domain.service.MoneyTransferService;
import com.minibank.quickpay.util.JsonUtil;
import spark.Route;

public class MoneyTransferHandler {

    public static Route transferMoney = (req, res) -> {
        res.type("application/json");
        MoneyTransferService moneyTransferService = new MoneyTransferService();
        MoneyTransferRequest moneyTransferRequest = JsonUtil.deserialize(req.body(), MoneyTransferRequest.class);
        moneyTransferService.transfer(moneyTransferRequest);
        res.status(200);
        return JsonUtil.serialize(new MoneyTransferResponse("Money successfully transferred."));
    };
}
