package com.x.sdk.test.util;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynList<T> {

    private LinkedList<T> list = new LinkedList<>();

    private Lock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition popCondition = lock.newCondition();

    public final int MAX_SIZE = 10;

    public void put(T t){
        lock.lock();
        try {
            while (MAX_SIZE == this.size()){
                putCondition.await();
            }
            list.add(t);
            popCondition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public T pop(){
        lock.lock();
        try {
            while (0 == this.size()){
                popCondition.await();
            }
            T obj = list.removeFirst();
            putCondition.signalAll();
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    public void putSyn(T t){
        synchronized(lock){
            while (MAX_SIZE == this.size()){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(t);
            lock.notifyAll();
        }
    }

    public T popSyn(){
        synchronized(lock){
            while (0 == this.size()){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = this.list.removeFirst();
            lock.notifyAll();
            return t;
        }
    }


    public int size(){
        return list.size();
    }

}
