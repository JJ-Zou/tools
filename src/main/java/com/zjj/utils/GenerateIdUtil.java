package com.zjj.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class GenerateIdUtil {

    private static final int OFFSET = 8;

    private static final AtomicInteger CNT = new AtomicInteger(0);

    public static long generateId() {
        return (System.currentTimeMillis() << OFFSET) | CNT.getAndIncrement();
    }

}
