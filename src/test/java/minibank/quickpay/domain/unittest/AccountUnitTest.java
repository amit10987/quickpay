package minibank.quickpay.domain.unittest;

import minibank.quickpay.domain.Account;
import minibank.quickpay.exception.InsufficientFund;
import minibank.quickpay.util.QuickPayMessages;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class AccountUnitTest {

    @Rule
    public ExpectedException expectedRule = ExpectedException.none();

    @Test
    public void should_throwIllegalArgumentException_when_OpeningBalanceIsNegativeTest() {
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_OPENING_BALANCE);
        new Account(new BigDecimal(-1), "John", 6578676756L);
    }

    @Test
    public void should_throwIllegalArgumentException_when_userNameIsNullTest() {
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_USER_NAME);
        new Account(new BigDecimal(500), null, 6578676756L);
    }

    @Test
    public void should_throwIllegalArgumentException_when_accountNumberIsNullTest() {
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_ACCOUNT_NUMBER);
        new Account(new BigDecimal(500), "John", null);
    }

    @Test
    public void should_throwIllegalArgumentException_when_accountNumberIsLessThan10digitTest() {
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_ACCOUNT_NUMBER);
        new Account(new BigDecimal(500), "John", 657867644L);
    }

    @Test
    public void should_createAccount_when_allTheMandatoryFieldProvidedTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        Assert.assertNotNull("Account instance should not be null", account);
        Assert.assertEquals("Account balance is inconsistent", new BigDecimal("500.00"), account.getBalance());
        Assert.assertEquals("Account Number did not match", new Long(6642159765L), account.getAccountNumber());
        Assert.assertEquals("User Name did not match", "John", account.getUserName());
    }

    @Test
    public void should_throwIllegalArgumentException_when_amountToBeCreditIsNullTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_CREDIT_AMOUNT);
        account.credit(null);
    }

    @Test
    public void should_throwIllegalArgumentException_when_amountToBeCreditIsNegativeValueTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_CREDIT_AMOUNT);
        account.credit(new BigDecimal(-1));
    }

    @Test
    public void should_creditAmount_when_amountToBeCreditIsPositiveDecimalValueTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.credit(new BigDecimal(23.78));
        Assert.assertEquals("After deposit the balance, amount is not consistent", new BigDecimal("523.78"), account.getBalance());
    }

    @Test
    public void should_throwIllegalArgumentException_when_amountToBeDebitIsNullTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_DEBIT_AMOUNT);
        account.debit(null);
    }

    @Test
    public void should_throwIllegalArgumentException_when_amountToBeDebitIsNegativeValueTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        expectedRule.expect(IllegalArgumentException.class);
        expectedRule.expectMessage(QuickPayMessages.INVALID_DEBIT_AMOUNT);
        account.debit(new BigDecimal(-1));
    }

    @Test
    public void should_debitAmount_when_amountToBeDebitIsPositiveDecimalValueTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.debit(new BigDecimal(25));
        Assert.assertEquals("After deposit the balance, amount is not consistent", new BigDecimal("475.00"), account.getBalance());
    }

    @Test
    public void should_throwInsufficientFundException_when_amountToBeDebitIsGreaterThanAccountBalanceTest() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        expectedRule.expect(InsufficientFund.class);
        expectedRule.expectMessage(QuickPayMessages.INSUFFICIENT_FUND);
        account.debit(new BigDecimal(501));
    }
}
