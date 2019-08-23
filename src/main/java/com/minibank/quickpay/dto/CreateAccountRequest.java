package com.minibank.quickpay.dto;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private String userName;
    private BigDecimal openingBalance;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }
}
