package com.minibank.quickpay.domain;

public class Account {

    private double balance;

    public double balance(){
        return balance;
    }

    public void credit(double amount){
        this.balance = this.balance + amount;
    }

    public void debit(double amount){
        this.balance = this.balance - amount;
    }
}
