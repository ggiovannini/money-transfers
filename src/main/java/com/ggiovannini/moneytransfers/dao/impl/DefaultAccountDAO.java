package com.ggiovannini.moneytransfers.dao.impl;

import com.ggiovannini.moneytransfers.dao.AccountDAO;
import com.ggiovannini.moneytransfers.datastore.DataStore;
import com.ggiovannini.moneytransfers.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAccountDAO implements AccountDAO {

    @Override
    public Account readAccount(String accountId) {
        return DataStore.getInstance().getAccounts().get(accountId);
    }

    @Override
    public List<Account> readAllAccounts() {
        return new ArrayList<>(DataStore.getInstance().getAccounts().values());
    }

    @Override
    public void createAccount(Account account) {
        DataStore.getInstance().getAccounts().putIfAbsent(account.getId(), account);
    }

    @Override
    public void deleteAllAccounts() {
        DataStore.getInstance().setAccounts(new ConcurrentHashMap());
    }
}
