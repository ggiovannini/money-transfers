package com.ggiovannini.moneytransfers.services.impl;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.exceptions.ApiException;
import com.ggiovannini.moneytransfers.dao.AccountDAO;
import com.ggiovannini.moneytransfers.dao.impl.DefaultAccountDAO;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import com.google.inject.Injector;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DefaultAccountServiceTest {

    private static AccountDAO accountDAOMock;
    private static DefaultAccountService service;
    private static Injector injector;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeClass
    public static void beforeAll() {
        Module testModule = Modules.override(new AppInjectorModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(AccountDAO.class).toInstance(mock(DefaultAccountDAO.class));
                    }
                });
        injector = Guice.createInjector(testModule);
        accountDAOMock = injector.getInstance(AccountDAO.class);
        service = injector.getInstance(DefaultAccountService.class);
    }

    @Test
    public void withdrawMoneySuccessfullyTest() throws ApiException {
        Account account = new Account(IdGeneratorUtil.generateId(), new BigDecimal("10.000"));
        BigDecimal amount = new BigDecimal("6.000");

        service.withdrawMoney(account, amount);

        assertEquals(new BigDecimal("4.000"), account.getBalance().getAmount());
    }

    @Test
    public void concurrentWithdrawMoneySuccessfullyTest() throws InterruptedException {
        Account account = new Account(IdGeneratorUtil.generateId(), new BigDecimal("200000"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 1000).forEach( c -> {
            executorService.submit(() -> {
                try {
                    service.withdrawMoney(account, new BigDecimal(100));
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            });
        });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(new BigDecimal("100000"), account.getBalance().getAmount());
    }

    @Test
    public void withdrawMoneyShouldThrowExceptionTest() throws ApiException {
        Account account = new Account(IdGeneratorUtil.generateId(), new BigDecimal("10.000"));
        BigDecimal amount = new BigDecimal("60.000");

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Insufficient account money to make the transaction");

        service.withdrawMoney(account, amount);
    }

    @Test
    public void depositMoneySuccessfullyTest() {
        Account account = new Account(IdGeneratorUtil.generateId(), new BigDecimal("10.000"));
        BigDecimal amount = new BigDecimal("60.000");

        service.depositMoney(account, amount);

        assertEquals(new BigDecimal("70.000"), account.getBalance().getAmount());
    }

    @Test
    public void concurrentDepositMoneySuccessfullyTest() throws InterruptedException {
        Account account = new Account(IdGeneratorUtil.generateId(), new BigDecimal("0"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 1000).forEach( c -> {
            executorService.submit(() -> {
                service.depositMoney(account, new BigDecimal(100));
            });
        });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        assertEquals(new BigDecimal("100000"), account.getBalance().getAmount());
    }

    @Test
    public void createAccountTestTest() {
        BigDecimal initAccountBalance = mock(BigDecimal.class);
        doNothing().when(accountDAOMock).createAccount(any());

        Account account = service.createAccount(initAccountBalance);

        verify(accountDAOMock).createAccount(any(Account.class));
        assertNotNull(account);
        assertEquals(initAccountBalance, account.getBalance().getAmount());
    }

    @Test
    public void searchAccountTest() {
        String accountId = "1234";
        Account accountMock = mock(Account.class);
        when(accountDAOMock.readAccount(accountId)).thenReturn(accountMock);

        Account account = service.searchAccount(accountId);

        verify(accountDAOMock).readAccount(accountId);
        assertEquals(accountMock, account);
    }
}
