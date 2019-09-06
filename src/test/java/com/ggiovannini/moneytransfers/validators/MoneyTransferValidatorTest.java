package com.ggiovannini.moneytransfers.validators;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoneyTransferValidatorTest {

    @Test
    public void isValidTest() {
        String rightSourceAccountId = "123123";
        String rightTargetAccountId = "572133";
        BigDecimal rightTransactionAmount = new BigDecimal(1000);

        MoneyTransferValidator validator = new MoneyTransferValidator(rightSourceAccountId, rightTargetAccountId, rightTransactionAmount);

        assertTrue(validator.isValid());
    }

    @Test
    public void isValidWithWrongAccountIdTest() {
        String wrongSourceAccountId = "";
        String rightTargetAccountId = "572133";
        BigDecimal rightTransactionAmount = new BigDecimal(1000);

        MoneyTransferValidator validator = new MoneyTransferValidator(wrongSourceAccountId, rightTargetAccountId, rightTransactionAmount);

        assertFalse(validator.isValid());
    }

    @Test
    public void isValidWithWrongAmountTest() {
        String rightSourceAccountId = "123123";
        String rightTargetAccountId = "572133";
        BigDecimal rightTransactionAmount = new BigDecimal(0);

        MoneyTransferValidator validator = new MoneyTransferValidator(rightSourceAccountId, rightTargetAccountId, rightTransactionAmount);

        assertFalse(validator.isValid());
    }
}
