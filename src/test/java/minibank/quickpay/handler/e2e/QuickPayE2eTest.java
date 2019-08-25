package minibank.quickpay.handler.e2e;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import minibank.quickpay.App;
import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.*;
import minibank.quickpay.util.JsonUtil;
import minibank.quickpay.util.QuickPayEndPoint;
import minibank.quickpay.util.QuickPayMessages;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuickPayE2eTest {

    public static class QuickPayApp implements SparkApplication {

        public void init() {
            App.init();
        }
    }

    @ClassRule
    public static final SparkServer<QuickPayApp> testServer = new SparkServer<>(QuickPayApp.class, 4568);

    @Test
    public void should_getAllAccount_when_getAllAccountEndPointCalledTest() throws HttpClientException {

        GetMethod getAllAccounts = testServer.get(QuickPayEndPoint.Account.GET_ALL_ACCOUNTS, false);
        getAllAccounts.addHeader("content-type", "application/json");
        HttpResponse httpResponse = testServer.execute(getAllAccounts);
        Assert.assertEquals(200, httpResponse.code());
        List<Account> accounts = JsonUtil.deserialize(new String(httpResponse.body()), ArrayList.class);
        Assert.assertTrue(accounts.size() > 0);
    }

    @Test
    public void should_getErrorResponse_whenOpeningBalanceIsNullTest() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setUserName("John");
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_OPENING_BALANCE, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenOpeningBalanceIsNegativeTest() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setUserName("John");
        createAccountRequest.setOpeningBalance(new BigDecimal(-200));
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_OPENING_BALANCE, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenUserNameIsNullTest() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(200));
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_USER_NAME, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenUserNameIsEmptyTest() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(200));
        createAccountRequest.setUserName("");
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_USER_NAME, errorResponse.getMessage());
    }

    @Test
    public void should_createNewAccount_validate_openingBalance_and_username_whenValidRequestProvidedTest() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal("200.00"));
        createAccountRequest.setUserName("John");
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(201, httpResponse.code());
        CreateAccountResponse createAccountResponse = JsonUtil.deserialize(new String(httpResponse.body()), CreateAccountResponse.class);
        Assert.assertNotNull("Account number should not be null", createAccountResponse.getAccountNumber());

        Account account = getAccount(createAccountResponse.getAccountNumber());
        Assert.assertEquals(createAccountRequest.getOpeningBalance(), account.getBalance());
        Assert.assertEquals(createAccountRequest.getUserName(), account.getUserName());
    }

    @Test
    public void should_getErrorMessageAccountNumberNotFound_whenWrongAccountNumberProvidedInTransferTest() throws HttpClientException {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setTransferAmount(new BigDecimal(200));
        moneyTransferRequest.setFromAccountNumber(12345L);
        moneyTransferRequest.setToAccountNumber(12346L);
        PostMethod transfer = testServer.post(QuickPayEndPoint.MoneyTransaction.TRANSFER, JsonUtil.serialize(moneyTransferRequest), false);
        HttpResponse httpResponse = testServer.execute(transfer);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.ACCOUNT_NUMBER_NOT_FOUND, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInvalidDebitAmount_whenTransferAmountIsNullInTransferTest() throws HttpClientException {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setFromAccountNumber(6642159765L);
        moneyTransferRequest.setToAccountNumber(6642159766L);
        PostMethod transfer = testServer.post(QuickPayEndPoint.MoneyTransaction.TRANSFER, JsonUtil.serialize(moneyTransferRequest), false);
        HttpResponse httpResponse = testServer.execute(transfer);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_DEBIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInvalidDebitAmount_whenTransferAmountIsNegativeInTransferTest() throws HttpClientException {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setTransferAmount(new BigDecimal(-200));
        moneyTransferRequest.setFromAccountNumber(6642159765L);
        moneyTransferRequest.setToAccountNumber(6642159766L);
        PostMethod transfer = testServer.post(QuickPayEndPoint.MoneyTransaction.TRANSFER, JsonUtil.serialize(moneyTransferRequest), false);
        HttpResponse httpResponse = testServer.execute(transfer);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_DEBIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInsufficientFund_whenTransferAmountIsGreaterThanAccountBalanceTest() throws HttpClientException {
        CreateAccountResponse fromAccount = createAccount("user1", new BigDecimal(200));
        CreateAccountResponse toAccount = createAccount("user2", new BigDecimal(200));

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setTransferAmount(new BigDecimal(300));
        moneyTransferRequest.setFromAccountNumber(fromAccount.getAccountNumber());
        moneyTransferRequest.setToAccountNumber(toAccount.getAccountNumber());

        PostMethod transfer = testServer.post(QuickPayEndPoint.MoneyTransaction.TRANSFER, JsonUtil.serialize(moneyTransferRequest), false);
        HttpResponse httpResponse = testServer.execute(transfer);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INSUFFICIENT_FUND, errorResponse.getMessage());
    }

    @Test
    public void should_transferSuccess_whenTransferAmountIsValidAndLessThanAccountBalanceTest() throws HttpClientException {
        CreateAccountResponse fromAccount = createAccount("user3", new BigDecimal(200));
        CreateAccountResponse toAccount = createAccount("user4", new BigDecimal(200));

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setTransferAmount(new BigDecimal(100));
        moneyTransferRequest.setFromAccountNumber(fromAccount.getAccountNumber());
        moneyTransferRequest.setToAccountNumber(toAccount.getAccountNumber());

        PostMethod transfer = testServer.post(QuickPayEndPoint.MoneyTransaction.TRANSFER, JsonUtil.serialize(moneyTransferRequest), false);
        HttpResponse httpResponse = testServer.execute(transfer);
        Assert.assertEquals(200, httpResponse.code());
        MoneyTransferResponse moneyTransferResponse = JsonUtil.deserialize(new String(httpResponse.body()), MoneyTransferResponse.class);
        Assert.assertEquals(QuickPayMessages.SUCCESSFUL_TRANSFER, moneyTransferResponse.getMessage());

        Account fromAccountAfterTransfer = getAccount(fromAccount.getAccountNumber());
        Account toAccountAfterTransfer = getAccount(toAccount.getAccountNumber());

        Assert.assertEquals(new BigDecimal("100.00"), fromAccountAfterTransfer.getBalance());
        Assert.assertEquals(new BigDecimal("300.00"), toAccountAfterTransfer.getBalance());
    }

    @Test
    public void should_getErrorMessageInvalidAmount_whenDepositAmountIsNullTest() throws HttpClientException {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);
        PostMethod deposit = testServer.post(QuickPayEndPoint.MoneyTransaction.DEPOSIT, JsonUtil.serialize(moneyDepositRequest), false);
        HttpResponse httpResponse = testServer.execute(deposit);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_CREDIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInvalidAmount_whenDepositAmountIsNegativeTest() throws HttpClientException {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);
        moneyDepositRequest.setAmount(new BigDecimal(-200));
        PostMethod deposit = testServer.post(QuickPayEndPoint.MoneyTransaction.DEPOSIT, JsonUtil.serialize(moneyDepositRequest), false);
        HttpResponse httpResponse = testServer.execute(deposit);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_CREDIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_depositSuccess_whenDepositAmountIsValidTest() throws HttpClientException {
        CreateAccountResponse depositAccount = createAccount("deposit user", new BigDecimal(200));

        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(depositAccount.getAccountNumber());
        moneyDepositRequest.setAmount(new BigDecimal(200));

        PostMethod deposit = testServer.post(QuickPayEndPoint.MoneyTransaction.DEPOSIT, JsonUtil.serialize(moneyDepositRequest), false);
        HttpResponse httpResponse = testServer.execute(deposit);

        Assert.assertEquals(200, httpResponse.code());
        MoneyDepositResponse moneyDepositResponse = JsonUtil.deserialize(new String(httpResponse.body()), MoneyDepositResponse.class);
        Assert.assertEquals(QuickPayMessages.SUCCESSFUL_DEPOSIT, moneyDepositResponse.getMessage());

        Account depositAmountAfterTransaction = getAccount(depositAccount.getAccountNumber());

        Assert.assertEquals(new BigDecimal("400.00"), depositAmountAfterTransaction.getBalance());

    }

    @Test
    public void should_getErrorMessageInvalidAmount_when_withdrawAmountIsNullTest() throws HttpClientException {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        PostMethod withdraw = testServer.post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, JsonUtil.serialize(moneyWithdrawRequest), false);
        HttpResponse httpResponse = testServer.execute(withdraw);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_DEBIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInvalidAmount_when_withdrawAmountIsNegativeValueTest() throws HttpClientException {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        moneyWithdrawRequest.setAmount(new BigDecimal(-200));
        PostMethod withdraw = testServer.post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, JsonUtil.serialize(moneyWithdrawRequest), false);
        HttpResponse httpResponse = testServer.execute(withdraw);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_DEBIT_AMOUNT, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorMessageInsufficientFund_when_withdrawAmountIsGreaterThanAccountBalanceTest() throws HttpClientException {
        CreateAccountResponse withdrawAccount = createAccount("deposit user", new BigDecimal(200));

        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(withdrawAccount.getAccountNumber());
        moneyWithdrawRequest.setAmount(new BigDecimal(300));

        PostMethod withdraw = testServer.post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, JsonUtil.serialize(moneyWithdrawRequest), false);
        HttpResponse httpResponse = testServer.execute(withdraw);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INSUFFICIENT_FUND, errorResponse.getMessage());
    }

    @Test
    public void should_successWithdraw_when_withdrawAmountIsGreaterThanAccountBalanceTest() throws HttpClientException {
        CreateAccountResponse withdrawAccount = createAccount("deposit user", new BigDecimal(200));

        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(withdrawAccount.getAccountNumber());
        moneyWithdrawRequest.setAmount(new BigDecimal(100));

        PostMethod withdraw = testServer.post(QuickPayEndPoint.MoneyTransaction.WITHDRAW, JsonUtil.serialize(moneyWithdrawRequest), false);
        HttpResponse httpResponse = testServer.execute(withdraw);
        Assert.assertEquals(200, httpResponse.code());
        MoneyWithdrawResponse moneyWithdrawResponse = JsonUtil.deserialize(new String(httpResponse.body()), MoneyWithdrawResponse.class);
        Assert.assertEquals(QuickPayMessages.SUCCESSFUL_WITHDRAW, moneyWithdrawResponse.getMessage());

        Account withdrawAccountAfterTransaction = getAccount(withdrawAccount.getAccountNumber());
        Assert.assertEquals(new BigDecimal("100.00"), withdrawAccountAfterTransaction.getBalance());
    }

    private Account getAccount(Long accountNumber) throws HttpClientException {
        GetMethod getAccount = testServer.get("/accounts/" + accountNumber, false);
        HttpResponse httpResponse = testServer.execute(getAccount);
        return JsonUtil.deserialize(new String(httpResponse.body()), Account.class);
    }

    private CreateAccountResponse createAccount(String userName, BigDecimal openingBalance) throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(openingBalance);
        createAccountRequest.setUserName(userName);
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        return JsonUtil.deserialize(new String(httpResponse.body()), CreateAccountResponse.class);
    }
}
