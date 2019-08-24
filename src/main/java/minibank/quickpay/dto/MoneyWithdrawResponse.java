package minibank.quickpay.dto;

public class MoneyWithdrawResponse {

    private String message;

    public MoneyWithdrawResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
