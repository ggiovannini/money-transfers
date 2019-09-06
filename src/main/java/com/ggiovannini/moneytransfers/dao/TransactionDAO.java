package com.ggiovannini.moneytransfers.dao;

import com.ggiovannini.moneytransfers.model.Transaction;


public interface TransactionDAO {

    /**
     * Obtains a Transaction from the data store
     *
     * @param transactionId
     * @return
     */
    Transaction readTransaction(String transactionId);

    /**
     * Creates a new transaction in the data store
     *
     * @param transaction
     */
    void createTransaction(Transaction transaction);

    /**
     * Deletes all the transactions in the data store
     */
    void deleteAllTransactions();
}
