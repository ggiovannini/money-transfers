package com.ggiovannini.moneytransfers.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGeneratorUtil {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    /**
     * Generates an unique id
     *
     * @return
     */
    public static String generateId() {
        return Integer.toString(ID_GENERATOR.incrementAndGet());
    }

    /**
     * Resets the id's generator.
     * It is used for test purposes
     */
    public static void resetIdGenerator() {
        ID_GENERATOR.updateAndGet(i -> 0);
    }
}
