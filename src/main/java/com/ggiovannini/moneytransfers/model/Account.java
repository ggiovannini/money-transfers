package com.ggiovannini.moneytransfers.model;

import java.math.BigDecimal;

//TODO: Support currencies
public class Account {
    private final String id;
    private MoneyBalance balance;

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = new MoneyBalance(balance);
    }

    public String getId() {
        return id;
    }

    public MoneyBalance getBalance() {
        return balance;
    }
}


