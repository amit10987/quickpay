package com.minibank.quickpay;


import com.minibank.quickpay.util.DbUtil;

import java.sql.SQLException;

import static spark.Spark.get;

public class App {

    public static void main(String[] args) throws SQLException {

        DbUtil.initialize();

        get("/account/health", (req, res) -> "health is OK");
    }
}
