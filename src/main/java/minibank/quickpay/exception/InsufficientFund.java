package minibank.quickpay.exception;

import minibank.quickpay.util.ErrorMessages;

public class InsufficientFund extends RuntimeException{
    public InsufficientFund(){
        super(ErrorMessages.INSUFFICIENT_FUND);
    }
}
