package com.minibank.quickpay;


import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        get("/account/balance", (req, res)->"Hello, world");
    }
}
