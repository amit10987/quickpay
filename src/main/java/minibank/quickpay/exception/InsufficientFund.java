package minibank.quickpay.exception;

import minibank.quickpay.util.QuickPayMessages;

public class InsufficientFund extends RuntimeException{
    public InsufficientFund(){
        super(QuickPayMessages.INSUFFICIENT_FUND);
    }
}
