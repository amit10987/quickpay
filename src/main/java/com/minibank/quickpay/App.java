package com.minibank.quickpay;


import com.minibank.quickpay.dto.ErrorResponse;
import com.minibank.quickpay.handler.AccountHandler;
import com.minibank.quickpay.handler.MoneyTransactionHandler;
import com.minibank.quickpay.util.DbUtil;
import com.minibank.quickpay.util.JsonUtil;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        DbUtil.initialize();

        post("/accounts", AccountHandler.createAccount);
        get("/accounts", AccountHandler.getAllAccounts);

        post("/transaction/transfer", MoneyTransactionHandler.transfer);
        post("/transaction/deposit", MoneyTransactionHandler.deposit);
        post("/transaction/withdraw", MoneyTransactionHandler.withdraw);

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
