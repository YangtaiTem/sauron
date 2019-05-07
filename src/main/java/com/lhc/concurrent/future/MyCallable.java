/**
 * 方法 cancle(boolean mayInterruptIfRunning) 的参数 mayInterruptIfRunning的作用是:
 *      如果线程正在运行则是否中断正在运行的线程，
 *      在代码中使用if(Thread.currentThread().isInterrupted())进行配合。
 * 方法 cancel()的返回值代表发送取消任务的命令是否成功完成
 */
package com.lhc.concurrent.future;

import java.util.concurrent.*;

public class MyCallable implements Callable{
    @Override
    public String call() throws Exception {
        boolean bool = true;
        while(bool){
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
            System.out.println("运行中");
        }
        return "hello world";
    }

    public static void main(String[] args) throws Exception{
        MyCallable myCallable = new MyCallable();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 5,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        Future future = pool.submit(myCallable);
        Thread.sleep(4000);
        System.out.println("cancel:" + future.cancel(true) + ",isCancelled:" + future.isCancelled());

    }
}
