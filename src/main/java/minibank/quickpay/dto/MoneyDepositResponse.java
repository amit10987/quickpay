package minibank.quickpay.dto;

public class MoneyDepositResponse {

    private String message;

    public MoneyDepositResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
