package com.ggiovannini.moneytransfers.router.impl;

import com.ggiovannini.moneytransfers.router.Router;

public class DefaultRouter extends Router {

    public static final int DEFAULT_APPLICATION_PORT = 8080;

    @Override
    public void init() {
        init(DEFAULT_APPLICATION_PORT);
    }

    public void init(int port) {
        setUpRouter(port);
    }
}
