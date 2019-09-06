package com.ggiovannini.moneytransfers.router.impl;

import com.ggiovannini.moneytransfers.router.Router;

public class TestRouter extends Router {

    public static final int TEST_APPLICATION_PORT = 8888;

    @Override
    public void init() {
        setUpRouter(TEST_APPLICATION_PORT);
    }
}
