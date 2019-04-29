/**
 * 启动线程数大于 corePoolSize + LinkedBlockingDeque.size 时，
 * 会启动非核心线程， 当启动数大于 maxSize + + LinkedBlockingDeque.size 时，
 * 多出来的任务不处理，抛出异常
 */
package com.lhc.concurrent.executor.queue;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyLinked {
    public static void main(String[] args){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                try{
                    System.out.println("begin " + System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("end " + System.currentTimeMillis());
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(2);
        System.out.println("deque "+ deque.size());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 3, 5,
                TimeUnit.SECONDS, deque);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        System.out.println("deque " + deque.size());
        System.out.println("poolSize " + executor.getPoolSize());
    }
}
