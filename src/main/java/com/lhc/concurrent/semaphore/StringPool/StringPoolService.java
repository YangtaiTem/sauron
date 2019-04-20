package com.lhc.concurrent.semaphore.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StringPoolService {
    private int poolMaxSize = 3;
    //同时可以有5个线程访问这个pool
    private int semaphorePermits = 5;

    private Semaphore semaphore = new Semaphore(semaphorePermits);
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private List<String> list = new ArrayList<>();

    public StringPoolService() {
        super();
        //初始化一个list
        for (int i = 0; i < poolMaxSize; i++){
            list.add("String " + i);
        }
    }

    public String get(){
        String getString = null;
        try {
            semaphore.acquire();
            //加锁，能保证这段代码同一时刻只有一个线程在运行
            lock.lock();
            while (list.size() == 0){
                //如果pool为空，等待
                condition.await();
            }
            getString = list.remove(0);
            //解锁
            lock.unlock();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return getString;
    }

    public void put(String str){
        //加锁
        lock.lock();
        list.add(str);
        //填充以后通知，get方法就不再await
        condition.signalAll();
        //解锁
        lock.unlock();
        semaphore.release();
    }
}
