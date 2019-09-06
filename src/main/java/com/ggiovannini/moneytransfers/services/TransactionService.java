package com.ggiovannini.moneytransfers.services;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.model.Transaction;

import java.math.BigDecimal;

public interface TransactionService {

    /**
     * Creates a new transaction
     *
     * @param sourceAccointId
     * @param targetAccountId
     * @param transactionAmount
     * @return
     */
    Transaction createTransaction(String sourceAccointId, String targetAccountId, BigDecimal transactionAmount);

    /**
     * Execute a transaction between two accounts
     *
     * @param transaction
     * @return
     * @throws ApiException
     * @throws ExternalException
     */
    Transaction makeTransference(Transaction transaction) throws ApiException, ExternalException;
}
