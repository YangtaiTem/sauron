package com.lhc.concurrent.semaphore.acquire;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class AcquireService {
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Semaphore semaphore = new Semaphore(8);

    public void doSomething() {
        try {
            System.out.println("有线程在等待这个许可:" + semaphore.hasQueuedThreads());
            System.out.println("有" + semaphore.getQueueLength() + "个线程在等待");
            /**
             * 方法 acquire( int permits ) 参数作用，及动态添加 permits 许可数量
             * 表示每调用一次这个方法，使用几个permit　　
             *　new Semaphore(8) 表示初始化了 8个通路， semaphore.acquire(2) 表示每次线程进入将会占用2个通路，
             *  semaphore.release(2) 运行时表示归还2个通路。没有通路，则线程就无法进入代码块。
             *　没有参数时，默认等价于参数是1
             * */
            semaphore.acquire(2);
            System.out.println(Thread.currentThread().getName() + ":开始执行,时间:" + getFormatTimeStr());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + ":执行结束,时间:" + getFormatTimeStr());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release(2);
        }
    }

    public static String getFormatTimeStr() {
        return sf.format(new Date());
    }
}
