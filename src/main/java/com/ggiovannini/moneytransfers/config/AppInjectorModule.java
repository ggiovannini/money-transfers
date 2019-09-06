package com.ggiovannini.moneytransfers.config;

import com.ggiovannini.moneytransfers.dao.TransactionDAO;
import com.ggiovannini.moneytransfers.dao.impl.DefaultAccountDAO;
import com.ggiovannini.moneytransfers.dao.AccountDAO;
import com.ggiovannini.moneytransfers.dao.impl.DefaultTransactionDAO;
import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.services.AccountService;
import com.ggiovannini.moneytransfers.services.TransactionService;
import com.ggiovannini.moneytransfers.services.impl.DefaultAccountService;
import com.ggiovannini.moneytransfers.services.impl.DefaultTransactionService;
import com.ggiovannini.moneytransfers.utils.SerializationUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;

public class AppInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bindServices();
        bindManagers();
        bindUtils();
    }

    protected void bindServices() {
        bind(AccountService.class).to(DefaultAccountService.class);
        bind(TransactionService.class).to(DefaultTransactionService.class);
    }

    protected void bindManagers() {
        bind(AccountDAO.class).to(DefaultAccountDAO.class);
        bind(TransactionDAO.class).to(DefaultTransactionDAO.class);

    }

    protected void bindUtils() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Account.class, SerializationUtils.buildAccountSerializer())
                .registerTypeAdapter(Account.class, SerializationUtils.buildAccountDeserializer());

        bind(Gson.class).toInstance(gsonBuilder.create());
    }
}
