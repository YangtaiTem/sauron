### ThreadPoolExecutor  线程池

1.1 线程池构造方法中各个参数的意义

```

/**
     * Creates a new {@code ThreadPoolExecutor} with the given initial parameters and default thread factory.
     *
     * @param corePoolSize the number of threads to keep in the pool, even if they are idle,   
     *          unless {@code allowCoreThreadTimeOut} is set
     *      核心线程数，即使空闲也会保存在线程池中，除非设置allowCoreThreadTimeOut
     * @param maximumPoolSize the maximum number of threads to allow in the pool
     *        线程中允许的最大线程数
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     *      核心线程之外的空闲线程的回收时间
     * @param unit the time unit for the {@code keepAliveTime} argument
     *      上面时间的单位
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     *      任务队列
     * @param threadFactory the factory to use when the executor creates a new thread
     *      创建线程时使用的工厂,Executors提供了Executors.defaultThreadFactory()
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     *      当处理程序由于已达到线程边界和队列容量而阻塞时，handler被使用
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             threadFactory, handler);
    }

````

测试类

```

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

```

1.2 关于 corePoolSize + LinkedBlockingDeque.size

```

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

```

2.1 shutdown() 和 shutdownNow()

    方法shutdown()的作用是使当前未执行完的线程继续执行，而不再添加新的任务，
    shutdown()方法是不阻塞的。
    方法shutdownNow()的作用是中断所有的任务,并且抛出InterruptdException
    异常（需要和任务中的if(Thread.currentThread().isInterrupted() == true) 结合使用）。
    未执行的线程不再执行，也从执行队列中清除。
    
3.1 工厂ThreadFactory + UncaughtExceptionHandler 处理异常

可以通过重写ThreadFactory 中的newThread(Runnable r)方法来对线程的一些属性进行定制化。 

3.2 代码

```

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

``` 

3.3 测试类

```

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

```   

3.4运行结果

> name:Mon May 13 17:16:57 GMT+08:00 2019 1557739017491
  java.lang.NullPointerException   
  自定义处理异常   
  name:Mon May 13 17:16:57 GMT+08:00 2019:null   
    at com.lhc.concurrent.executor.factory.MyRunnable.run(MyRunnable.java:13)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at             
    java.lang.Thread.run(Thread.java:745)      

4.1 方法afterExecute() 和 beforeExecute() 

   在线程池ThreadPoolExecutor 类中重写这两个方法可以对线程池中执行的线程对象实现监控。
   
4.2 代码

```

package com.lhc.concurrent.executor.before;

public class MyThread extends Thread{
    public MyThread(String name) {
        super();
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println("打印了! begin" + this.getName() + " " + System.currentTimeMillis());
        try {
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("打印了! end" + this.getName() + " " + System.currentTimeMillis());
    }
}

```

4.3 测试类

```

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


```

11.1 SingleThreadExecutor的意义

> SingleThreadExecutor内部会创建一个Thread，这个Thread的工作就是从一个队列中取出用户提交的任务进行执行，
如果执行过程中发生未受检的异常，singleThreadExecutor会自动重新启动一个线程再继续工作，这一点比自己创建  
一个线程自己管理轻松很多，也不需要再去维护一个任务队列。

>简单说下线程池管理的线程的几点意义：
1、缓存线程、进行池化，可实现线程重复利用、避免重复创建和销毁所带来的性能开销。
2、当线程调度任务出现异常时，会重新创建一个线程替代掉发生异常的线程。
3、任务执行按照规定的调度规则执行。线程池通过队列形式来接收任务。再通过空闲线程来逐一取出进行任务调度。即线程池可以控制任务调度的执行顺序。
4、可制定拒绝策略。即任务队列已满时，后来任务的拒绝处理规则。
以上意义对于singleThreadExecutor来说也是适用的。普通线程和线程池中创建的线程其最大的区别就是有无一个管理者对线程进行管理。

12.1 方法execute() 与submit的区别

    1) 方法execute() 没有返回值，而submit()方法可以有返回值
    2) 方法execute() 在默认的情况下异常直接抛出，不能捕获，但可以通过自定义ThreadFactory 的方式进行捕获，而submit()方法在默认的情况下，可以
    捕获异常。