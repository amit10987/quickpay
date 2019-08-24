package minibank.quickpay.domain;


import minibank.quickpay.exception.InsufficientFund;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_OpeningBalanceIsNegative() {
        new Account(new BigDecimal(-1), "John", 6578676756L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_userNameIsNull() {
        new Account(new BigDecimal(500), null, 6578676756L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_accountNumberIsNull() {
        new Account(new BigDecimal(500), "John", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_accountNumberIsLessThan10digit() {
        new Account(new BigDecimal(500), "John", 657867644L);
    }

    @Test
    public void should_createAccount_when_allTheMandatoryFieldProvided() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        Assert.assertNotNull("Account instance should not be null", account);
        Assert.assertEquals("Account balance is inconsistent", new BigDecimal("500.00"), account.getBalance());
        Assert.assertEquals("Account Number did not match", new Long(6642159765L), account.getAccountNumber());
        Assert.assertEquals("User Name did not match", "John", account.getUserName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_amountToBeCreditIsNull() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.credit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_amountToBeCreditIsNegativeValue() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.credit(new BigDecimal(-1));
    }

    @Test
    public void should_creditAmount_when_amountToBeCreditIsPositiveDecimalValue() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.credit(new BigDecimal(23.78));
        Assert.assertEquals("After deposit the balance, amount is not consistent", new BigDecimal("523.78"), account.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_amountToBeDebitIsNull() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.debit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throwIllegalArgumentException_when_amountToBeDebitIsNegativeValue() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.debit(new BigDecimal(-1));
    }

    @Test
    public void should_debitAmount_when_amountToBeDebitIsPositiveDecimalValue() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.debit(new BigDecimal(25));
        Assert.assertEquals("After deposit the balance, amount is not consistent", new BigDecimal("475.00"), account.getBalance());
    }

    @Test(expected = InsufficientFund.class)
    public void should_throwInsufficientFundException_when_amountToBeDebitIsGreaterThanAccountBalance() {
        Account account = new Account(new BigDecimal(500), "John", 6642159765L);
        account.debit(new BigDecimal(501));
    }
}
