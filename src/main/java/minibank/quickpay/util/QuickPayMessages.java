package minibank.quickpay.util;

public final class QuickPayMessages {
    private QuickPayMessages() {
    }

    public static final String INSUFFICIENT_FUND = "Insufficient Fund";
    public static final String INVALID_CREDIT_AMOUNT = "Not a valid amount to credit";
    public static final String INVALID_DEBIT_AMOUNT = "Not a valid amount to debit";
    public static final String INVALID_ACCOUNT_NUMBER = "Account number must not be empty or less than 10 digit";
    public static final String INVALID_USER_NAME = "User Name must not be empty";
    public static final String INVALID_OPENING_BALANCE = "Opening balance is mandatory and must not be negative";
    public static final String SUCCESSFUL_TRANSFER = "Money successfully transferred";
    public static final String SUCCESSFUL_DEPOSIT = "Money successfully deposited";
    public static final String SUCCESSFUL_WITHDRAW = "Money successfully withdraw";
    public static final String ACCOUNT_NUMBER_NOT_FOUND = "Account number does not exist";

}
