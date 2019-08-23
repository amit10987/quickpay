package com.minibank.quickpay;


import com.minibank.quickpay.dto.ErrorResponse;
import com.minibank.quickpay.handler.AccountHandler;
import com.minibank.quickpay.handler.MoneyTransferHandler;
import com.minibank.quickpay.util.DbUtil;
import com.minibank.quickpay.util.JsonUtil;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        DbUtil.initialize();
        get("/accounts/health", (req, res) -> "health is OK");
        post("/accounts", AccountHandler.createAccount);
        get("/accounts", AccountHandler.getAllAccounts);

        post("/moneytransfer", MoneyTransferHandler.transferMoney);

        notFound((req, res) -> {
            res.type("application/json");
            return JsonUtil.serialize(new ErrorResponse("Requested resource is not found."));
        });

        internalServerError((req, res) -> {
            res.type("application/json");
            return JsonUtil.serialize(new ErrorResponse("Internal server error."));
        });
    }
}
