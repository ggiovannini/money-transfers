package com.ggiovannini.moneytransfers.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MoneyBalance {
    private BigDecimal amount;

    public MoneyBalance() {
        this(new BigDecimal(BigInteger.ZERO));
    }

    public MoneyBalance(BigDecimal balanceAmount) {
        amount = balanceAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
