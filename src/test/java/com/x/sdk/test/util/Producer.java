package com.x.sdk.test.util;


public class Producer implements Runnable{

    private SynList synList;
    public boolean flag = true;


    public Producer(SynList synList){
        this.synList = synList;
    }

    @Override
    public void run() {
        while (flag){
//            System.out.println("Producer "+Thread.currentThread().getName()+" put start");
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synList.put(new Object());
            System.out.println("list size is "+synList.size());
//            System.out.println("Producer "+Thread.currentThread().getName()+" put end");
            System.out.println();
        }
        System.out.println("Producer is done!!!");
    }
}
