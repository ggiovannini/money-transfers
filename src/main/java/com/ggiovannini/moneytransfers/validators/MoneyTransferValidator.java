package com.ggiovannini.moneytransfers.validators;

import spark.utils.StringUtils;

import java.math.BigDecimal;

public class MoneyTransferValidator implements Validable {

    private String sourceAccount;
    private String targetAccount;
    private BigDecimal transactionAmount;

    public MoneyTransferValidator(String sourceAccount, String targetAccount, BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(sourceAccount)
                && StringUtils.isNotBlank(targetAccount)
                && transactionAmount != null
                && transactionAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }
}
