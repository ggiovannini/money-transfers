package com.ggiovannini.moneytransfers.router;


import com.ggiovannini.moneytransfers.controllers.AccountController;
import com.ggiovannini.moneytransfers.controllers.TransactionController;
import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.model.ErrorResponse;
import com.ggiovannini.moneytransfers.utils.LoggerUtils;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

import static spark.Spark.*;


public abstract class Router implements SparkApplication {

    @Inject
    protected TransactionController transactionController;

    @Inject
    protected AccountController accountController;

    @Inject
    protected Gson gson;

    protected Logger logger = LoggerFactory.getLogger("");


    @Override
    public void destroy() {
        stop();
    }

    public void setUpRouter(int port) {
        port(port);

        setUpRoutes();
        setUpExceptionMapping();

        before((request, response) -> {
            response.type("application/json");
            logger.info(LoggerUtils.requestInfoToString(request));
        });

        after((request, response) -> logger.info(LoggerUtils.responseInfoToString(response)));

        notFound((req, res) ->  "{\"message\":\"Resource not found\"}");
        internalServerError((req, res) -> "{\"message\":\"Custom 500 handling\"}");
    }

    protected void setUpRoutes() {
        // Account endpoints
        get("api/1.0/account/:accountId", accountController::getAccount, gson::toJson);
        post("api/1.0/account", accountController::createAccount, gson::toJson);

        // Transaction endpoint
        post("api/1.0/transaction/transfer", transactionController::moneyTransfer, gson::toJson);
    }

    protected void setUpExceptionMapping() {
        exception(ExternalException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            String errorResponseJson = gson.toJson(new ErrorResponse(Integer.toString(exception.getStatus()), exception.getMessage()));
            logger.info(LoggerUtils.responseErrorToString(exception.getStatus(), errorResponseJson));
            response.body(errorResponseJson);
        });

        exception(ApiException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            String errorResponseJson = gson.toJson(new ErrorResponse(Integer.toString(exception.getStatus()), exception.getMessage()));
            logger.error(LoggerUtils.responseErrorToString(exception.getStatus(), errorResponseJson));
            response.body(errorResponseJson);
        });
    }
}
