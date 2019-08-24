package minibank.quickpay.exception;

public class InsufficientFund extends RuntimeException{
    public InsufficientFund(){
        super("Insufficient Fund");
    }
}
