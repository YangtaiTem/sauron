package com.lhc.concurrent.executor.param;

import java.util.concurrent.*;

public class Constructor {
    public static void main(String[] args) throws InterruptedException{
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " run ! " + System.currentTimeMillis());
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        ThreadPoolExecutor executor = getPoolBySynchronousQueue();

        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);

        Thread.sleep(300);

        System.out.println("first out core:" + executor.getCorePoolSize());
        System.out.println("first out :" + executor.getPoolSize());
        System.out.println("first out queue:" + executor.getQueue().size());

        Thread.sleep(10000);

        System.out.println("second out core:" + executor.getCorePoolSize());
        System.out.println("second out :" + executor.getPoolSize());
        System.out.println("second out queue:" + executor.getQueue().size());
    }

    /**
     * 如果使用LinkedBlockingDeque来构造，，当线程数量大于corePoolSize时，
     * 其余的任务直接放入队列中，maximumPoolSize参数的作用忽略
     * keepAliveTime参数的作用忽略
     * @return
     */
    public static ThreadPoolExecutor getPoolByLinkedBlockingDeque(){
        return new ThreadPoolExecutor(7, 8, 5,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    /**
     * 如果使用SynchronousQueue来构造，maximumPoolSize参数的作用生效
     * keepAliveTime参数的作用生效
     * 当启动线程大于maximumPoolSize参数时，不会放入队列，会因为无法处理的任务直接抛出异常
     * @return
     */
    public static ThreadPoolExecutor getPoolBySynchronousQueue(){
        return new ThreadPoolExecutor(7, 8, 5,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
}
