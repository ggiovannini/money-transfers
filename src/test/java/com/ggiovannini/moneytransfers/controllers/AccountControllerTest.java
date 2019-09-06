package com.ggiovannini.moneytransfers.controllers;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.services.impl.DefaultAccountService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private static AccountService accountServiceMock;
    private static AccountController controller;
    private static Injector injector;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeClass
    public static void beforeAll() {
        Module testModule = Modules.override(new AppInjectorModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(AccountService.class).toInstance(mock(DefaultAccountService.class));
                    }
                });
        injector = Guice.createInjector(testModule);
        accountServiceMock = injector.getInstance(AccountService.class);
        controller = injector.getInstance(AccountController.class);
    }

    @Test
    public void createAccountWithInvalidParameterTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);

        when(requestMock.body()).thenReturn("{\"balance\":\"-1\"}");

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("Invalid request body parameter");

        controller.createAccount(requestMock, responseMock);
    }

    @Test
    public void createAccountWithErrorTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);
        BigDecimal initialBalance = new BigDecimal(0);

        when(requestMock.body()).thenReturn("{\"balance\":\"0\"}");
        when(accountServiceMock.createAccount(initialBalance)).thenReturn(null);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("There was an error creating the account.");

        controller.createAccount(requestMock, responseMock);
    }

    @Test
    public void createAccountSuccessfulyTest() throws ApiException, ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);
        Account accountMock = mock(Account.class);

        when(requestMock.body()).thenReturn("{\"balance\":\"0\"}");
        when(accountServiceMock.createAccount(any(BigDecimal.class))).thenReturn(accountMock);
        doNothing().when(responseMock).status(201);

        Account accountResult = controller.createAccount(requestMock, responseMock);

        assertEquals(accountMock, accountResult);
        verify(requestMock).body();
        verify(responseMock).status(201);
    }

    @Test
    public void getAccountNotFoundTest() throws ExternalException {
        String accountId = "accountIdTest1";
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);

        when(requestMock.params("accountId")).thenReturn(accountId);
        when(accountServiceMock.searchAccount(accountId)).thenReturn(null);

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("Account with id '"+accountId+"' not found");

        controller.getAccount(requestMock, responseMock);
    }

    @Test
    public void getAccountWithNullIdTest() throws ExternalException {
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);

        when(requestMock.params("accountId")).thenReturn(null);

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("The account id cannot be null");

        controller.getAccount(requestMock, responseMock);
    }

    @Test
    public void getAccountSuccessfullyTest() throws ExternalException {
        String accountId = "accountIdTest2";
        Account accountMock = mock(Account.class);
        Request requestMock = mock(Request.class);
        Response responseMock = mock(Response.class);

        when(requestMock.params("accountId")).thenReturn(accountId);
        when(accountServiceMock.searchAccount(accountId)).thenReturn(accountMock);

        Account accountResult = controller.getAccount(requestMock, responseMock);

        assertEquals(accountMock, accountResult);
        verify(requestMock).params("accountId");
        verify(accountServiceMock).searchAccount(accountId);
    }
}
