package com.ggiovannini.moneytransfers.controllers;

import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ApiExceptionStatus;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.exceptions.ExternalExceptionStatus;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.validators.CreateAccountValidator;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class AccountController {

    public static final String ACCOUNT_PARAM_KEY = "accountId";

    @Inject
    private AccountService accountService;

    @Inject
    private Gson gson;

    public Account createAccount(Request request, Response response) throws ExternalException, ApiException {
        CreateAccountValidator validator = gson.fromJson(request.body(), CreateAccountValidator.class);

        if (!validator.isValid()) {
            throw new ExternalException("Invalid request body parameter", ExternalExceptionStatus.BAD_REQUEST);
        }

        Account account = accountService.createAccount(validator.getBalance());

        if (account == null) {
            throw new ApiException("There was an error creating the account.", ApiExceptionStatus.INTERNAL_SERVER_ERROR);
        }

        response.status(201);

        return account;
    }

    public Account getAccount(Request request, Response response) throws ExternalException {
        String accountId = request.params(ACCOUNT_PARAM_KEY);

        if (StringUtils.isBlank(accountId)) {
            throw new ExternalException("The account id cannot be null", ExternalExceptionStatus.BAD_REQUEST);
        }

        Account account = accountService.searchAccount(accountId);

        if (account == null) {
            throw new ExternalException("Account with id '"+accountId+"' not found", ExternalExceptionStatus.NOT_FOUND);
        }

        return account;
    }
}
