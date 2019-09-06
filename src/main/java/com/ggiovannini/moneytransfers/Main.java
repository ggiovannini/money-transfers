package com.ggiovannini.moneytransfers;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.router.impl.DefaultRouter;
import com.ggiovannini.moneytransfers.router.Router;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String... args) {
        Injector injector = Guice.createInjector(new AppInjectorModule());
        Router router = injector.getInstance(DefaultRouter.class);
        router.init();
    }
}
