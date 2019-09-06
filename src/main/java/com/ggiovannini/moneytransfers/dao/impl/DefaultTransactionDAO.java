package com.ggiovannini.moneytransfers.dao.impl;

import com.ggiovannini.moneytransfers.dao.TransactionDAO;
import com.ggiovannini.moneytransfers.datastore.DataStore;
import com.ggiovannini.moneytransfers.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransactionDAO implements TransactionDAO {

    @Override
    public Transaction readTransaction(String transactionId) {
        return DataStore.getInstance().getTransactions().get(transactionId);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        DataStore.getInstance().getTransactions().put(transaction.getId(), transaction);
    }

    @Override
    public void deleteAllTransactions() {
        DataStore.getInstance().setTransactions(new ConcurrentHashMap());
    }
}
