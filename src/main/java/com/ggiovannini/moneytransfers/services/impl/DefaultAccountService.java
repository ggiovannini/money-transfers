package com.ggiovannini.moneytransfers.services.impl;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ApiExceptionStatus;
import com.ggiovannini.moneytransfers.dao.AccountDAO;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.MoneyBalance;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class DefaultAccountService implements AccountService {

    @Inject
    private AccountDAO accountDAO;

    @Override
    public Account createAccount(BigDecimal balance) {
        Account account = new Account(IdGeneratorUtil.generateId(), balance);
        accountDAO.createAccount(account);
        return account;
    }

    @Override
    public Account searchAccount(String accountId) {
        return accountDAO.readAccount(accountId);
    }

    @Override
    public void withdrawMoney(Account account, BigDecimal amount) throws ApiException {
        MoneyBalance balance = account.getBalance();

        synchronized (balance) {
            BigDecimal currentBalance = balance.getAmount();

            if (currentBalance.compareTo(amount) < 0) {
                throw new ApiException("Insufficient account money to make the transaction", ApiExceptionStatus.INTERNAL_SERVER_ERROR);
            }

            balance.setAmount(currentBalance.add(amount.negate()));
        }
    }

    @Override
    public void depositMoney(Account account, BigDecimal amount) {
        MoneyBalance balance = account.getBalance();
        synchronized (balance) {
            balance.setAmount(balance.getAmount().add(amount));
        }
    }
}
