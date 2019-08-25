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
import minibank.quickpay.util.ErrorMessages;
import minibank.quickpay.util.JsonUtil;
import minibank.quickpay.util.QuickPayEndPoint;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountHandlerE2e {


    public static class AccountHandlerE2eSparkApp implements SparkApplication {
        public void init() {
            App.init();
        }
    }

    @ClassRule
    public static final SparkServer<AccountHandlerE2eSparkApp> testServer = new SparkServer<>(AccountHandlerE2e.AccountHandlerE2eSparkApp.class, 4568);

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
        Assert.assertEquals(ErrorMessages.INVALID_OPENING_BALANCE, errorResponse.getMessage());
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
        Assert.assertEquals(ErrorMessages.INVALID_OPENING_BALANCE, errorResponse.getMessage());
    }

    @Test
    public void should_getErrorResponse_whenUserNameIsNull() throws HttpClientException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(200));
        PostMethod createAccount = testServer.post(QuickPayEndPoint.Account.CREATE_ACCOUNT, JsonUtil.serialize(createAccountRequest), false);
        HttpResponse httpResponse = testServer.execute(createAccount);
        Assert.assertEquals(200, httpResponse.code());
        ErrorResponse errorResponse = JsonUtil.deserialize(new String(httpResponse.body()), ErrorResponse.class);
        Assert.assertEquals(ErrorMessages.INVALID_USER_NAME, errorResponse.getMessage());
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
        Assert.assertEquals(ErrorMessages.INVALID_USER_NAME, errorResponse.getMessage());
    }

    @Test
    public void should_createNewAccount_whenValidRequestProvided() throws HttpClientException {
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

    private Account getAccount(Long accountNumber) throws HttpClientException {
        GetMethod getAccount = testServer.get("/accounts/" + accountNumber, false);
        HttpResponse httpResponse = testServer.execute(getAccount);
        return JsonUtil.deserialize(new String(httpResponse.body()), Account.class);
    }
}
