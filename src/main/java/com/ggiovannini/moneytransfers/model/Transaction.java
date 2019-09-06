package com.ggiovannini.moneytransfers.model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: Support timestamp
public class Transaction {
    private final String id;
    private String sourceAccount;
    private String targetAccount;
    private TransactionStatus status;
    private BigDecimal transactionAmount;

    private static AtomicInteger idGenerator = new AtomicInteger(0);


    public Transaction(String sourceAccount, String targetAccount, BigDecimal transactionAmount) {
        this.id = Integer.toString(idGenerator.incrementAndGet());
        this.sourceAccount = sourceAccount;
        this.status = TransactionStatus.PENDING;
        this.transactionAmount = transactionAmount;
        this.targetAccount = targetAccount;
    }

    public String getId() {
        return id;
    }

    public TransactionStatus getStatus() {return status;}

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getTargetAccount() {return targetAccount; }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void updateStatus(TransactionStatus status) {this.status = status;}
}

