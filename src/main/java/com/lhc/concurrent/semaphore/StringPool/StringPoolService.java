package com.lhc.concurrent.semaphore.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StringPoolService {
    private int poolMaxSize = 3;
    private int semaphorePermits = 5;

    private Semaphore semaphore = new Semaphore(semaphorePermits);
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private List<String> list = new ArrayList<>();

    public StringPoolService() {
        super();
        for (int i = 0; i < poolMaxSize; i++){
            list.add("String " + i);
        }
    }

    public String get(){
        String getString = null;
        try {
            semaphore.acquire();
            lock.lock();
            while (list.size() == 0){
                condition.await();
            }
            getString = list.remove(0);
            lock.unlock();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return getString;
    }

    public void put(String str){
        lock.lock();
        list.add(str);
        condition.signalAll();
        lock.unlock();
        semaphore.release();
    }
}
