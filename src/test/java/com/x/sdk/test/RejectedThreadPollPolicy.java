package com.x.sdk.test;

import jdk.nashorn.internal.ir.Terminal;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedThreadPollPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//        System.out.println(Thread.currentThread().getName()+" is terminated");
    }
}
