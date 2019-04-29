package com.lhc.concurrent.executor.before;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool extends ThreadPoolExecutor{
    public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                        BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println(((MyThread)r).getName() + "准备执行");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.println(((MyThread)r).getName() + "执行完毕");
    }

    public static void main(String[] args){
        MyThreadPool myThreadPool = new MyThreadPool(2, 2, Integer.MAX_VALUE,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        myThreadPool.execute(new MyThread("0001"));
        myThreadPool.execute(new MyThread("0002"));
        myThreadPool.execute(new MyThread("0003"));
        myThreadPool.execute(new MyThread("0004"));
    }
}
