package minibank.quickpay.handler.e2e;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import minibank.quickpay.App;
import minibank.quickpay.domain.Account;
import minibank.quickpay.dto.CreateAccountRequest;
import minibank.quickpay.dto.CreateAccountResponse;
import minibank.quickpay.dto.ErrorResponse;
import minibank.quickpay.dto.MoneyTransferRequest;
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
    public void should_getAllAccount_when_getAllAccountEndPointCalled() throws HttpClientException {

        GetMethod getAllAccounts = testServer.get(QuickPayEndPoint.Account.GET_ALL_ACCOUNTS, false);
        getAllAccounts.addHeader("content-type", "application/json");
        HttpResponse httpResponse = testServer.execute(getAllAccounts);
        Assert.assertEquals(200, httpResponse.code());
        List<Account> accounts = JsonUtil.deserialize(new String(httpResponse.body()), ArrayList.class);
        Assert.assertTrue(accounts.size() > 0);
    }

    @Test
    public void should_getErrorResponse_whenOpeningBalanceIsNull() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setUserName("John");
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_OPENING_BALANCE, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenOpeningBalanceIsNegative() throws HttpClientException {
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
    public void should_getErrorResponse_whenUserNameIsNull() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(200));
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(QuickPayMessages.INVALID_USER_NAME, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenUserNameIsEmpty() throws HttpClientException {
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
    public void should_createNewAccount_validate_openingBalance_and_username_whenValidRequestProvided() throws HttpClientException {
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
    public void should_getErrorMessageAccountNumberNotFound_whenWrongAccountNumberProvidedInTransfer() throws HttpClientException {
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
    public void should_getErrorMessageInvalidDebitAmount_whenTransferAmountIsNullInTransfer() throws HttpClientException {
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
    public void should_getErrorMessageInvalidDebitAmount_whenTransferAmountIsNegativeInTransfer() throws HttpClientException {
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

    private Account getAccount(Long accountNumber) throws HttpClientException {
        GetMethod getAccount = testServer.get("/accounts/" + accountNumber, false);
        HttpResponse httpResponse = testServer.execute(getAccount);
        return JsonUtil.deserialize(new String(httpResponse.body()), Account.class);
    }
}
