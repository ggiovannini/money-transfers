package com.ggiovannini.moneytransfers.validators;

import java.math.BigDecimal;

public class CreateAccountValidator implements Validable {

    private BigDecimal balance;

    public CreateAccountValidator(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean isValid() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) >= 0;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
