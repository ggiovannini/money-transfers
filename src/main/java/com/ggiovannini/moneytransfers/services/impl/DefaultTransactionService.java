package com.ggiovannini.moneytransfers.services.impl;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.exceptions.ExternalExceptionStatus;
import com.ggiovannini.moneytransfers.dao.TransactionDAO;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.Transaction;
import com.ggiovannini.moneytransfers.model.TransactionStatus;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.services.TransactionService;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class DefaultTransactionService implements TransactionService {

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionDAO transactionDAO;

    @Override
    public Transaction createTransaction(String sourceAccointId, String targetAccountId, BigDecimal transactionAmount) {
        Transaction transaction = new Transaction(sourceAccointId, targetAccountId, transactionAmount);
        transactionDAO.createTransaction(transaction);
        return transaction;
    }

    @Override
    public Transaction makeTransference(Transaction transaction) throws ApiException, ExternalException {
        Account fromAccount = accountService.searchAccount(transaction.getSourceAccount());
        Account toAccount = accountService.searchAccount(transaction.getTargetAccount());

        if (fromAccount == null) {
            transaction.updateStatus(TransactionStatus.CANCELED);
            throw new ExternalException("Account with id '"+transaction.getSourceAccount()+"' not found", ExternalExceptionStatus.NOT_FOUND);
        }
        if (toAccount == null) {
            transaction.updateStatus(TransactionStatus.CANCELED);
            throw new ExternalException("Account with id '"+transaction.getTargetAccount()+"' not found", ExternalExceptionStatus.NOT_FOUND);
        }

        try {
            accountService.withdrawMoney(fromAccount, transaction.getTransactionAmount());
            accountService.depositMoney(toAccount, transaction.getTransactionAmount());
        } catch (ApiException e) {
            transaction.updateStatus(TransactionStatus.CANCELED);
            throw e;
        }

        transaction.updateStatus(TransactionStatus.DONE);

        return transaction;
    }
}
