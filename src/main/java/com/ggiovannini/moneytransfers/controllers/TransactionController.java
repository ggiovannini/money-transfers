package com.ggiovannini.moneytransfers.controllers;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ApiExceptionStatus;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.exceptions.ExternalExceptionStatus;
import com.ggiovannini.moneytransfers.model.Transaction;
import com.ggiovannini.moneytransfers.model.TransactionStatus;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.services.TransactionService;
import com.ggiovannini.moneytransfers.validators.MoneyTransferValidator;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

public class TransactionController {

    @Inject
    private TransactionService transactionService;

    @Inject
    private AccountService accountService;

    @Inject
    private Gson gson;

    public Transaction moneyTransfer(Request request, Response response) throws ExternalException, ApiException {
        MoneyTransferValidator validator = gson.fromJson(request.body(), MoneyTransferValidator.class);

        if (!validator.isValid()) {
            throw new ExternalException("Invalid request body parameter", ExternalExceptionStatus.BAD_REQUEST);
        }

        Transaction transaction = transactionService.createTransaction(validator.getSourceAccount(), validator.getTargetAccount(), validator.getTransactionAmount());

        transactionService.makeTransference(transaction);

        if (TransactionStatus.CANCELED.equals(transaction.getStatus())) {
            throw new ApiException("The transaction was canceled", ApiExceptionStatus.INTERNAL_SERVER_ERROR);
        }

        return transaction;
    }
}
