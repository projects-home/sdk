package com.x.sdk.test;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {

//    ExecutorService service = Executors.newFixedThreadPool(5);

    LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(50);
    ThreadPoolExecutor poolExecutor  = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
            blockingQueue, new RejectedThreadPollPolicy());

   public static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args){
        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
//        Random r = new Random();
////        for(int i=0; i<10; i++){
////            Future<?> future = threadPoolTest.service.submit(new Callable<Object>() {
////                @Override
////                public Object call() throws Exception {
////                    try {
////                        Thread.sleep(r.nextInt(10)*1000);
////                        System.out.println(Thread.currentThread().getName()+" done");
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                    return (Integer)1;
////                }
////            });
//////            try {
//////                Thread.sleep(10*1000);
//////            } catch (InterruptedException e) {
//////                e.printStackTrace();
//////            }
////        }
////        threadPoolTest.service.shutdown();
////        while (true){
////            try {
////                Thread.sleep(1*1000);
////                System.out.println("---------threadPool state is "+threadPoolTest.service.toString());
////
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
////
//////        queueTest.service.submit()

        Object o = new Object();

        for(int i=0; i<100; i++){
            threadPoolTest.poolExecutor.execute(()->{
                    try {

                        synchronized (o){
                            Thread.sleep(3*1000);
                        }

                        System.out.println(Thread.currentThread().getName()+" "+threadPoolTest.atomicInteger.incrementAndGet()+" done");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                });
            System.out.println(i +" is done-----");
        }
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread poll is shutdwon");
        threadPoolTest.poolExecutor.shutdownNow();

        while (true){
            try {

                synchronized (o){
                    System.out.println("get o");
                }

                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-------"+threadPoolTest.poolExecutor.toString());
        }
    }

}
