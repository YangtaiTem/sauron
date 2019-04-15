package com.lhc.concurrent.semaphore.tryAcquire;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TryAcquireService {
    private Semaphore semaphore = new Semaphore(8);

    public void doSomething() {
        try {
            /**
             * tryAcquire(int permits)
             * Acquires the given number of permits from this semaphore, only if all are available at the time of invocation.
             * 尝试去从这个信号量获取指定数量的在调用时都是可用的许可 。
             * 如果不使用permits 参数，tryAcquire()表示获取一个许可。
             */
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
            /**
             * tryAcquire(int permits, long timeout, TimeUnit unit)
             * Acquires the given number of permits from this semaphore,
             * if all become available within the given waiting time and the current thread has not been interrupted.
             * 在指定的时间内尝试去从这个信号量获取指定数量的许可 ，同时这段时间内，这个线程没有被中断。
             * 如果不使用permits 参数，tryAcquire(long timeout, TimeUnit unit)表示获取一个许可。
             */
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
