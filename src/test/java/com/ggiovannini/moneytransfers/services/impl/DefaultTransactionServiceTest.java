package com.ggiovannini.moneytransfers.services.impl;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.exceptions.ApiExceptionStatus;
import com.ggiovannini.moneytransfers.exceptions.ExternalException;
import com.ggiovannini.moneytransfers.dao.TransactionDAO;
import com.ggiovannini.moneytransfers.dao.impl.DefaultTransactionDAO;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.Transaction;
import com.ggiovannini.moneytransfers.model.TransactionStatus;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class DefaultTransactionServiceTest {


    private static TransactionDAO transactionDAOMock;
    private static AccountService accountServiceMock;
    private static DefaultTransactionService service;
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
                        bind(TransactionDAO.class).toInstance(mock(DefaultTransactionDAO.class));
                    }
                });
        injector = Guice.createInjector(testModule);
        accountServiceMock = injector.getInstance(AccountService.class);
        service = injector.getInstance(DefaultTransactionService.class);
        transactionDAOMock = injector.getInstance(TransactionDAO.class);
    }

    @Test
    public void makeTransferenceSuccessfullyTest() throws ExternalException, ApiException {
        String sourceAccountId = "12356";
        String targetAccountId = "99887";
        BigDecimal transactionAmount = new BigDecimal("9000");
        Transaction transaction = new Transaction(sourceAccountId, targetAccountId, transactionAmount);
        Account fromAccountMock = mock(Account.class);
        Account toAccountMock = mock(Account.class);

        when(accountServiceMock.searchAccount(sourceAccountId)).thenReturn(fromAccountMock);
        when(accountServiceMock.searchAccount(targetAccountId)).thenReturn(toAccountMock);
        doNothing().when(accountServiceMock).withdrawMoney(fromAccountMock, transactionAmount);
        doNothing().when(accountServiceMock).depositMoney(toAccountMock, transactionAmount);

        assertEquals(TransactionStatus.PENDING, transaction.getStatus());

        service.makeTransference(transaction);

        assertEquals(TransactionStatus.DONE, transaction.getStatus());
        verify(accountServiceMock).searchAccount(sourceAccountId);
        verify(accountServiceMock).searchAccount(targetAccountId);
        verify(accountServiceMock).withdrawMoney(fromAccountMock, transactionAmount);
        verify(accountServiceMock).depositMoney(toAccountMock, transactionAmount);
    }

    @Test
    public void makeTransferenceWithSourceAccountNotFoundTest() throws ExternalException, ApiException {
        String sourceAccountId = "45096";
        String targetAccountId = "657654";
        BigDecimal transactionAmount = new BigDecimal("9000");
        Transaction transaction = new Transaction(sourceAccountId, targetAccountId, transactionAmount);
        Account fromAccountMock = mock(Account.class);

        when(accountServiceMock.searchAccount(sourceAccountId)).thenReturn(fromAccountMock);
        when(accountServiceMock.searchAccount(targetAccountId)).thenReturn(null);

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("Account with id '" + targetAccountId + "' not found");

        service.makeTransference(transaction);
    }

    @Test
    public void makeTransferenceWithTargetAccountNotFoundTest() throws ExternalException, ApiException {
        String sourceAccountId = "788789";
        String targetAccountId = "324321";
        BigDecimal transactionAmount = new BigDecimal("9000");
        Transaction transaction = new Transaction(sourceAccountId, targetAccountId, transactionAmount);
        Account toAccountMock = mock(Account.class);

        when(accountServiceMock.searchAccount(sourceAccountId)).thenReturn(null);
        when(accountServiceMock.searchAccount(targetAccountId)).thenReturn(toAccountMock);

        exceptionRule.expect(ExternalException.class);
        exceptionRule.expectMessage("Account with id '" + sourceAccountId + "' not found");

        service.makeTransference(transaction);
    }

    @Test
    public void makeTransferenceWithInsufficentAccountMoneyTest() throws ExternalException, ApiException {
        String sourceAccountId = "120983";
        String targetAccountId = "987916";
        BigDecimal transactionAmount = new BigDecimal("9000");
        Transaction transaction = new Transaction(sourceAccountId, targetAccountId, transactionAmount);
        Account fromAccountMock = mock(Account.class);
        Account toAccountMock = mock(Account.class);
        ApiException exception = new ApiException("Insufficient account money to make the transaction", ApiExceptionStatus.INTERNAL_SERVER_ERROR);

        when(accountServiceMock.searchAccount(sourceAccountId)).thenReturn(fromAccountMock);
        when(accountServiceMock.searchAccount(targetAccountId)).thenReturn(toAccountMock);
        doThrow(exception).when(accountServiceMock).withdrawMoney(fromAccountMock, transactionAmount);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage(exception.getMessage());

        service.makeTransference(transaction);

    }

    @Test
    public void createTransactionTest() {
        String sourceAccountId = "989898";
        String targetAccountId = "235124";
        BigDecimal transactionAmount = BigDecimal.TEN;

        doNothing().when(transactionDAOMock).createTransaction(any());

        Transaction transactionExpected = service.createTransaction(sourceAccountId, targetAccountId, transactionAmount);

        assertNotNull(transactionExpected.getId());
        assertEquals(sourceAccountId, transactionExpected.getSourceAccount());
        assertEquals(targetAccountId, transactionExpected.getTargetAccount());
        assertEquals(transactionAmount, transactionExpected.getTransactionAmount());
        verify(transactionDAOMock).createTransaction(any());
    }
}
