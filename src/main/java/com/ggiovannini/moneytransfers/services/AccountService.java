package com.ggiovannini.moneytransfers.services;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    /***
     * Gets the account with the specified identification
     *
     * @param accountId
     * @return
     */
    Account searchAccount(String accountId);

    /**
     * Creates an account with the initial balance specified
     *
     * @param balance: Initial balance
     * @return The account created
     */
    Account createAccount(BigDecimal balance);

    /**
     * Deposits the specified money amount to the specified account
     *
     * @param account The account to deposits the money
     * @param amount The money to deposit
     */
    void depositMoney(Account account, BigDecimal amount);

    /**
     * Withdraws the specified money amount from the specified account
     *
     * @param account
     * @param amount
     * @throws ApiException
     */
    void withdrawMoney(Account account, BigDecimal amount) throws ApiException;
}
