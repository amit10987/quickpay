package minibank.quickpay.domain.service.functionaltest;

import minibank.quickpay.domain.Account;
import minibank.quickpay.domain.service.MoneyTransactionServiceImpl;
import minibank.quickpay.dto.MoneyDepositRequest;
import minibank.quickpay.dto.MoneyTransferRequest;
import minibank.quickpay.dto.MoneyWithdrawRequest;
import minibank.quickpay.exception.InsufficientFund;
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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransactionServiceFunctionalTest {

    @InjectMocks
    MoneyTransactionServiceImpl moneyTransactionService;

    @Mock
    private AccountRepositoryImpl accountRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public ExpectedException expectedRule = ExpectedException.none();

    @Test
    public void should_throwInsufficientFundException_when_transferAmountIsGreaterThanAccountBalance() {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setFromAccountNumber(6642159765L);
        moneyTransferRequest.setToAccountNumber(6642159766L);
        moneyTransferRequest.setTransferAmount(new BigDecimal(600));

        Account fromAccount = new Account(new BigDecimal(500), "John", moneyTransferRequest.getFromAccountNumber());
        Account toAccount = new Account(new BigDecimal(500), "John", moneyTransferRequest.getToAccountNumber());

        when(accountRepository.findByAccountNumber(eq(moneyTransferRequest.getFromAccountNumber()), any())).thenReturn(fromAccount);
        when(accountRepository.findByAccountNumber(eq(moneyTransferRequest.getToAccountNumber()), any())).thenReturn(toAccount);

        expectedRule.expect(InsufficientFund.class);
        expectedRule.expectMessage(ErrorMessages.INSUFFICIENT_FUND);

        moneyTransactionService.transfer(moneyTransferRequest);
    }

    @Test
    public void should_success_when_transferAmountIsLessThanAccountBalance() {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setFromAccountNumber(6642159765L);
        moneyTransferRequest.setToAccountNumber(6642159766L);
        moneyTransferRequest.setTransferAmount(new BigDecimal(400));

        Account fromAccount = new Account(new BigDecimal(500), "John", moneyTransferRequest.getFromAccountNumber());
        Account toAccount = new Account(new BigDecimal(500), "John", moneyTransferRequest.getToAccountNumber());

        when(accountRepository.findByAccountNumber(eq(moneyTransferRequest.getFromAccountNumber()), any())).thenReturn(fromAccount);
        when(accountRepository.findByAccountNumber(eq(moneyTransferRequest.getToAccountNumber()), any())).thenReturn(toAccount);
        moneyTransactionService.transfer(moneyTransferRequest);

        Assert.assertEquals("Account balance should be 100 after debiting", new BigDecimal("100.00"), fromAccount.getBalance());
        Assert.assertEquals("Account balance should be 900 after crediting", new BigDecimal("900.00"), toAccount.getBalance());
    }

    @Test
    public void should_throwIllegalArgumentException_when_depositAmountIsNegativeValue() {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);
        moneyDepositRequest.setAmount(new BigDecimal(-200));

        Account account = new Account(new BigDecimal(500), "John", moneyDepositRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyDepositRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_CREDIT_AMOUNT);

        moneyTransactionService.deposit(moneyDepositRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_when_depositAmountIsZero() {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);
        moneyDepositRequest.setAmount(new BigDecimal(0));

        Account account = new Account(new BigDecimal(500), "John", moneyDepositRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyDepositRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_CREDIT_AMOUNT);

        moneyTransactionService.deposit(moneyDepositRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_when_depositAmountIsNull() {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);

        Account account = new Account(new BigDecimal(500), "John", moneyDepositRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyDepositRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_CREDIT_AMOUNT);

        moneyTransactionService.deposit(moneyDepositRequest);
    }

    @Test
    public void should_success_when_depositAmountIsPositiveDecimalValue() {
        MoneyDepositRequest moneyDepositRequest = new MoneyDepositRequest();
        moneyDepositRequest.setAccountNumber(6642159765L);
        moneyDepositRequest.setAmount(new BigDecimal(100));

        Account account = new Account(new BigDecimal(500), "John", moneyDepositRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyDepositRequest.getAccountNumber()), any())).thenReturn(account);

        moneyTransactionService.deposit(moneyDepositRequest);

        Assert.assertEquals("Account balance should be 600 after crediting", new BigDecimal("600.00"), account.getBalance());
    }

    @Test
    public void should_throwIllegalArgumentException_when_creditAmountIsZero() {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        moneyWithdrawRequest.setAmount(new BigDecimal(0));

        Account account = new Account(new BigDecimal(500), "John", moneyWithdrawRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyWithdrawRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_DEBIT_AMOUNT);

        moneyTransactionService.withdraw(moneyWithdrawRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_when_creditAmountIsNull() {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);

        Account account = new Account(new BigDecimal(500), "John", moneyWithdrawRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyWithdrawRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_DEBIT_AMOUNT);

        moneyTransactionService.withdraw(moneyWithdrawRequest);
    }

    @Test
    public void should_throwIllegalArgumentException_when_creditAmountIsNegativeValue() {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        moneyWithdrawRequest.setAmount(new BigDecimal(-200));

        Account account = new Account(new BigDecimal(500), "John", moneyWithdrawRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyWithdrawRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(ErrorMessages.INVALID_DEBIT_AMOUNT);

        moneyTransactionService.withdraw(moneyWithdrawRequest);
    }

    @Test
    public void should_success_when_withdrawAmountIsPositiveDecimalValue_And_LessThanAvailableBalance() {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        moneyWithdrawRequest.setAmount(new BigDecimal(200));

        Account account = new Account(new BigDecimal(500), "John", moneyWithdrawRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyWithdrawRequest.getAccountNumber()), any())).thenReturn(account);

        moneyTransactionService.withdraw(moneyWithdrawRequest);

        Assert.assertEquals("Account balance should be 300 after debiting", new BigDecimal("300.00"), account.getBalance());
    }

    @Test
    public void should_throwInsufficientFund_when_withdrawAmountIsPositiveDecimalValue_And_GreaterThanAvailableBalance() {
        MoneyWithdrawRequest moneyWithdrawRequest = new MoneyWithdrawRequest();
        moneyWithdrawRequest.setAccountNumber(6642159765L);
        moneyWithdrawRequest.setAmount(new BigDecimal(600));

        Account account = new Account(new BigDecimal(500), "John", moneyWithdrawRequest.getAccountNumber());
        when(accountRepository.findByAccountNumber(eq(moneyWithdrawRequest.getAccountNumber()), any())).thenReturn(account);

        expectedRule.expect(InsufficientFund.class);
        expectedRule.expectMessage(ErrorMessages.INSUFFICIENT_FUND);

        moneyTransactionService.withdraw(moneyWithdrawRequest);
    }
}
