package minibank.quickpay.exception;

import minibank.quickpay.util.QuickPayMessages;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
        super(QuickPayMessages.ACCOUNT_NUMBER_NOT_FOUND);
    }
}
