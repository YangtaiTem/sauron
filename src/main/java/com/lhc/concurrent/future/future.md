### Future 和 Callable

1.1 Callable 和 Runnable 的区别

    1.Callable 接口的call()方法可以有返回值，而Runnable接口的run()方法没有返回值。执行完Callable
    接口中的任务后，返回值是通过接口进行获得的。
    2.Callable 接口的call()方法可以声明抛出异常，而Runnale的接口run()方法不可以声明抛出异常。

1.2 方法cancel(boolean b) 和 isCancelled()的使用
    
    方法cancel(boolean b)的参数 b 的作用是：如果线程正在运行则是否中断正在运行的线程，在代码中使用if(Thread.currentThread().isInterrupted())
    进行配合。
    方法cancel()的返回值代表发送取消任务的命令是否成功完成
    
1.3 代码

```

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


```
    
1.4 测试结果

> ...   
  运行中   
  运行中   
  运行中   
  运行中   
  运行中   
  运行中   
  cancel:true,isCancelled:true
    