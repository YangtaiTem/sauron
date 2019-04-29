package com.lhc.concurrent.executor.factory;

import java.util.Date;
import java.util.concurrent.ThreadFactory;

public class MyTHreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("name:" + new Date());
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("自定义处理异常");
                System.out.println(t.getName() + ":" + e.getMessage());
                e.printStackTrace();
            }
        });
        return thread;
    }
}
