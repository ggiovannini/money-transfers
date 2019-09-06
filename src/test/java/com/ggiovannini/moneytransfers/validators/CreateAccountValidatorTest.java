package com.ggiovannini.moneytransfers.validators;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateAccountValidatorTest {

    @Test
    public void isValidTest() {
        BigDecimal rightAmount = new BigDecimal(0);

        CreateAccountValidator validator = new CreateAccountValidator(rightAmount);

        assertTrue(validator.isValid());
    }

    @Test
    public void isValidWithWrongAmountTest() {
        BigDecimal wrongAmount = new BigDecimal(-1);

        CreateAccountValidator validator = new CreateAccountValidator(wrongAmount);

        assertFalse(validator.isValid());
    }
}
