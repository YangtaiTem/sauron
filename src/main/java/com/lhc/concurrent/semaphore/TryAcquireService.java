package com.lhc.concurrent.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TryAcquireService {
    private Semaphore semaphore = new Semaphore(8);

    public void doSomething() {
        try {
            if (semaphore.tryAcquire(2)) {
                System.out.println(Thread.currentThread().getName() + "获得锁,时间:" + System.currentTimeMillis());
                Thread.sleep(100);
                semaphore.release(2);
            }else {
                System.out.println(Thread.currentThread().getName() + "没有获得锁,时间:" + System.currentTimeMillis());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void doThing() {
        try {
            if (semaphore.tryAcquire(2, 1, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + "获得锁,时间:" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "释放锁,时间:" + System.currentTimeMillis());
                semaphore.release(2);
            }else {
                System.out.println(Thread.currentThread().getName() + "没有获得锁,时间:" + System.currentTimeMillis());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
