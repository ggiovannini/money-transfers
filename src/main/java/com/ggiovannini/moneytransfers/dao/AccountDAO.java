package com.ggiovannini.moneytransfers.dao;

import com.ggiovannini.moneytransfers.model.Account;

import java.util.List;

public interface AccountDAO {

    /**
     * Obtains an account from the data store
     *
     * @param accountId
     * @return
     */
    Account readAccount(String accountId);

    /**
     * Obtains all the accounts from the data store
     *
     * @return
     */
    List<Account> readAllAccounts();

    /**
     * Creates a new Account in the data store
     *
     * @param account
     */
    void createAccount(Account account);

    /**
     * Delete all the accounts in the data store
     */
    void deleteAllAccounts();
}
