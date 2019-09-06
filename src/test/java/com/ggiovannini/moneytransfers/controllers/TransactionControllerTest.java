package com.ggiovannini.moneytransfers.controllers;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.model.Transaction;
import com.ggiovannini.moneytransfers.model.TransactionStatus;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.services.TransactionService;
import com.ggiovannini.moneytransfers.services.impl.DefaultAccountService;
import com.ggiovannini.moneytransfers.services.impl.DefaultTransactionService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {
    private static Injector injector;

    private static TransactionService transactionServiceMock;
    private static AccountService accountServiceMock;
    private static TransactionController controller;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeClass
    public static void beforeAll() {
        Module testModule = Modules.override(new AppInjectorModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(AccountService.class).toInstance(mock(DefaultAccountService.class));
                        bind(TransactionService.class).toInstance(mock(DefaultTransactionService.class));
                        bind(TransactionController.class).toInstance(spy(TransactionController.class));

                    }
                });
        injector = Guice.createInjector(testModule);
        accountServiceMock = injector.getInstance(AccountService.class);
        transactionServiceMock = injector.getInstance(TransactionService.class);
        controller = injector.getInstance(TransactionController.class);
    }

    @Test
    public void moneyTransferInvalidRequestParametersTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);

        when(requestMock.body()).thenReturn("{\"transaction_amount\":\"0\",\"source_account\":\"334545\",\"target_account\":\"46123412\"}");

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("Invalid request body parameter");

        controller.moneyTransfer(requestMock, responseMock);
    }

    @Test
    public void moneyTransferWithApiExceptionInTransactionServiceTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);
        Transaction transactionMock = mock(Transaction.class);
        String sourceAccountId = "132145";
        String targetAccountId = "413412";
        BigDecimal amount = new BigDecimal(90903);

        when(transactionServiceMock.createTransaction(sourceAccountId, targetAccountId , amount)).thenReturn(transactionMock);
        when(requestMock.body()).thenReturn("{\"transaction_amount\":\"90903\",\"source_account\":\"132145\",\"target_account\":\"413412\"}");
        doThrow(ApiException.class).when(transactionServiceMock).makeTransference(transactionMock);

        exceptionRule.expect(ApiException.class);

        controller.moneyTransfer(requestMock, responseMock);
    }

    @Test
    public void moneyTransferCanceledTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);
        Transaction transactionMock = mock(Transaction.class);

        when(requestMock.body()).thenReturn("{\"transaction_amount\":\"9090\",\"source_account\":\"134345\",\"target_account\":\"46412\"}");
        when(transactionServiceMock.createTransaction(anyString(), anyString(),any())).thenReturn(transactionMock);
        when(transactionServiceMock.makeTransference(transactionMock)).thenReturn(transactionMock);
        when(transactionMock.getStatus()).thenReturn(TransactionStatus.CANCELED);

        exceptionRule.expect(ApiException.class);

        controller.moneyTransfer(requestMock, responseMock);
    }

    @Test
    public void moneyTransferSuccessfullyTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);
        String sourceAccountId = "99212";
        String targetAccountId = "99431212";
        BigDecimal amount = new BigDecimal(1000);
        Transaction transactionMock = mock(Transaction.class);

        when(requestMock.body()).thenReturn("{\"transaction_amount\":\"1000\",\"source_account\":\"99212\",\"target_account\":\"99431212\"}");
        when(transactionServiceMock.createTransaction(anyString(), anyString(),any())).thenReturn(transactionMock);
        when(transactionServiceMock.makeTransference(transactionMock)).thenReturn(transactionMock);
        when(transactionMock.getStatus()).thenReturn(TransactionStatus.DONE);

        Transaction transaction = controller.moneyTransfer(requestMock, responseMock);

        assertNotNull(transaction);
        verify(transactionServiceMock).createTransaction(sourceAccountId, targetAccountId, amount);
        verify(transactionServiceMock).makeTransference(transactionMock);
        verify(transactionMock).getStatus();
    }
}
