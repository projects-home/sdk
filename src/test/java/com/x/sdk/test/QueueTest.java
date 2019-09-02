package com.x.sdk.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {

    ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(10);
    public LinkedBlockingQueue<String> queuse = new LinkedBlockingQueue<>(1000);

    public static void main(String[] args){

        QueueTest queueTest = new QueueTest();

        for (int i=0; i<100; i++){
            try {
                queueTest.queue.put(new Object());
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
