package com.ggiovannini.moneytransfers.datastore;

import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;


/**
 * This class represents the data store and it's very simple for the shake of the test.
 * It is very simple with the purpose of performing this test, but in a real project, it would be a database.
 */
public class DataStore {

    private static ConcurrentHashMap<String, Account> accounts;
    private static ConcurrentHashMap<String, Transaction> transactions;

    private static DataStore INSTANCE;

    private DataStore() {
        accounts = new ConcurrentHashMap();
        transactions = new ConcurrentHashMap();
    }

    public static DataStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataStore();
        }
        return INSTANCE;
    }

    public static ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }

    public static void setAccounts(ConcurrentHashMap<String, Account> accounts) {
        DataStore.accounts = accounts;
    }

    public static ConcurrentHashMap<String, Transaction> getTransactions() {
        return transactions;
    }

    public static void setTransactions(ConcurrentHashMap<String, Transaction> transactions) {
        DataStore.transactions = transactions;
    }
}
