package com.lhc.concurrent.semaphore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class ConstructionService {

    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 同步关键类
     * permits 是允许许可的意思
     * 构造方法传入的数字是多少，则同一个时刻，只运行多少个进程同时运行指定代码
     * 指定代码就是 在 semaphore.acquire() 和 semaphore.release()之间的代码
     */
    private Semaphore semaphore = new Semaphore(2);

    public void doSomething() {
        try {
            /**
             * 在 semaphore.acquire() 和 semaphore.release()之间的代码，同一时刻只允许指定个数的线程进入，
             * 因为semaphore的构造方法是1，则同一时刻只允许一个线程进入，其他线程只能等待。
             * */
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + ":开始执行,时间:" + getFormatTimeStr());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + ":执行结束,时间:" + getFormatTimeStr());
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getFormatTimeStr() {
        return sf.format(new Date());
    }
}
