package com.minibank.quickpay.exception;

public class AccountNotFoundException extends RuntimeException{

    public  AccountNotFoundException(){
        super("Account number does not exist.");
    }
}
