package com.lhc.concurrent.executor.factory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis());
        //抛出异常
        String str = null;
        System.out.println(str.contains(""));
        System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis());
    }

    public static void main(String[] srgs){
        MyRunnable myRunnable = new MyRunnable();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 20, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
        executor.setThreadFactory(new MyTHreadFactory());
        executor.execute(myRunnable);
    }
}
