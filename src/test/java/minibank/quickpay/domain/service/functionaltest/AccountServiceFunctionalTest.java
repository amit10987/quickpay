package minibank.quickpay.domain.service.functionaltest;

import minibank.quickpay.domain.service.AccountServiceImpl;
import minibank.quickpay.dto.CreateAccountRequest;
import minibank.quickpay.infrastructure.AccountRepositoryImpl;
import minibank.quickpay.util.ErrorMessages;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceFunctionalTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepositoryImpl accountRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public ExpectedException expectedRule = ExpectedException.none();

    @Test
    public void should_createAccountWithValidAccountNumber_when_correctValueProvided() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(5));
        createAccountRequest.setUserName("John");
        Long accountNumber = accountService.createAccount(createAccountRequest);
        Assert.assertEquals("Account number should be 10 digit", 10, accountNumber.toString().length());
    }

    @Test
    public void should_throwIllegalArgumentException_whenUserNameIsNotSetInRequest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(5));
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_USER_NAME);
        accountService.createAccount(createAccountRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_whenUserNameIsEmptyString() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(5));
        createAccountRequest.setUserName("");
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_USER_NAME);
        accountService.createAccount(createAccountRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_whenOpeningBalanceIsNotSetInRequest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setUserName("John");
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_OPENING_BALANCE);
        accountService.createAccount(createAccountRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_whenOpeningBalanceIsNegative() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOpeningBalance(new BigDecimal(-1));
        createAccountRequest.setUserName("John");
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_OPENING_BALANCE);
        accountService.createAccount(createAccountRequest);
    }
}
