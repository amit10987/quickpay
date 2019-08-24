package minibank.quickpay.dto;

public class MoneyTransferResponse {

    private String message;

    public MoneyTransferResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
