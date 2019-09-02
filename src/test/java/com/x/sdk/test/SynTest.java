package com.x.sdk.test;

import com.x.sdk.test.util.Consumer;
import com.x.sdk.test.util.Producer;
import com.x.sdk.test.util.SynList;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynTest implements Runnable{

    Lock lock = new ReentrantLock();

    @Override
    public void run() {

    }

    public static void main(String[] args){

        SynList<Object> synList = new SynList();
        Thread t = null;
        for(int i=0; i<3; i++){
           t = new Thread(new Producer(synList), String.valueOf(i));
           t.start();
        }

        Thread[] consumer = new Thread[5];
        for(int i=0; i<consumer.length; i++){
            consumer[i] = new Thread(new Consumer(synList), String.valueOf(i));
            consumer[i].start();
        }

        while (true){
            try {
                Thread.sleep(5*1000);
                for(int i=0; i<consumer.length; i++){
                    System.out.println(consumer[i].getName()+" state is "+ consumer[i].getState());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
