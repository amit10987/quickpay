package minibank.quickpay.dto;

public class CreateAccountResponse {

    private Long accountNumber;

    public CreateAccountResponse(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }
}
