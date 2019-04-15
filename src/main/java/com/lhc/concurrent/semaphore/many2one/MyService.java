package com.lhc.concurrent.semaphore.many2one;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class MyService {
    private Semaphore semaphore = new Semaphore(3);
    private ReentrantLock lock = new ReentrantLock();

    public void doThing(){
        try {
            semaphore.acquire();
            System.out.println("ThreadName = " + Thread.currentThread().getName()
                    + "准备doThing:" + System.currentTimeMillis());
            lock.lock();
            for (int i = 0; i < 5; i ++){
                System.out.println("ThreadName = " + Thread.currentThread().getName()
                        + "打印" + (i + 1) + " " + System.currentTimeMillis());
            }
            System.out.println("doThing结束:" + System.currentTimeMillis());
            lock.unlock();
            semaphore.release();
            System.out.println("ThreadName = " + Thread.currentThread().getName()
                    + "结束");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
