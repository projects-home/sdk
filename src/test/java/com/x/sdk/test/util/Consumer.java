package com.x.sdk.test.util;

public class Consumer implements Runnable{

    private SynList synList;
    public boolean flag = true;

    public Consumer(SynList synList){
        this.synList = synList;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Consumer " + Thread.currentThread().getName() + " consumming "+this.synList.pop());
        }
    }
}
