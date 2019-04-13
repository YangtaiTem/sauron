### Semaphore 信号量

1.构造方法

    同步关键类 Semaphore
    permits 是允许许可的意思
    构造方法传入的数字是多少，则同一个时刻，只运行多少个进程同时运行指定代码
    指定代码就是 在 semaphore.acquire() 和 semaphore.release()之间的代码
    private Semaphore semaphore = new Semaphore(2);


```

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

```

2.测试类

```

package com.lhc.concurrent.semaphore;

public class SemaphoreThread extends Thread{
    private ConstructionService constructionService;

    public SemaphoreThread(ConstructionService constructionService, String name) {
        super();
        this.constructionService = constructionService;
        this.setName(name);
    }

    public static void main(String[] args) {
        ConstructionService constructionService = new ConstructionService();
        for (int i = 0; i < 10; i++) {
            SemaphoreThread semaphoreThread = new SemaphoreThread(constructionService, "线程" + i);
            semaphoreThread.start();
        }
    }

    @Override
    public void run() {
        constructionService.doSomething();
    }
}


```

3.执行结果

`
线程0:开始执行,时间:2019-04-13 10:08:44.290
线程1:开始执行,时间:2019-04-13 10:08:44.290
线程1:执行结束,时间:2019-04-13 10:08:46.294
线程0:执行结束,时间:2019-04-13 10:08:46.294
线程3:开始执行,时间:2019-04-13 10:08:46.294
线程2:开始执行,时间:2019-04-13 10:08:46.294
线程2:执行结束,时间:2019-04-13 10:08:48.306
线程3:执行结束,时间:2019-04-13 10:08:48.306
线程5:开始执行,时间:2019-04-13 10:08:48.306
线程6:开始执行,时间:2019-04-13 10:08:48.306
线程6:执行结束,时间:2019-04-13 10:08:50.318
线程5:执行结束,时间:2019-04-13 10:08:50.318
线程4:开始执行,时间:2019-04-13 10:08:50.318
线程7:开始执行,时间:2019-04-13 10:08:50.318
线程7:执行结束,时间:2019-04-13 10:08:52.320
线程4:执行结束,时间:2019-04-13 10:08:52.320
线程8:开始执行,时间:2019-04-13 10:08:52.320
线程9:开始执行,时间:2019-04-13 10:08:52.320
线程9:执行结束,时间:2019-04-13 10:08:54.324
线程8:执行结束,时间:2019-04-13 10:08:54.324
`

11.方法 acquire( int permits ) 和 release( int permits )
    
    方法 acquire( int permits ) 参数作用，及动态添加 permits 许可数量
    表示每调用一次这个方法，使用几个permit　　
    new Semaphore(8) 表示初始化了 8个通路， semaphore.acquire(2) 表示每次线程进入将会占用2个通路，
    semaphore.release(2) 运行时表示归还2个通路。没有通路，则线程就无法进入代码块。
    没有参数时，默认等价于参数是1。

    
```

package com.lhc.concurrent.semaphore;

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

```

12.测试类

```

package com.lhc.concurrent.semaphore;

public class AcquireThread extends Thread{
    private AcquireService acquireService;

    public AcquireThread(AcquireService acquireService, String name) {
        super();
        this.acquireService = acquireService;
        this.setName(name);
    }

    public static void main(String[] args){
        AcquireService acquireService = new AcquireService();
        for (int i = 0; i < 10; i++) {
            AcquireThread acquireThread = new AcquireThread(acquireService, "线程" + i);
            acquireThread.start();
        }
    }




    @Override
    public void run() {
        acquireService.doSomething();
    }

    public AcquireService getAcquireService() {
        return acquireService;
    }

    public void setAcquireService(AcquireService acquireService) {
        this.acquireService = acquireService;
    }
}

```

13.测试结果

`
有线程在等待这个许可:false
有线程在等待这个许可:false
有0个线程在等待
有线程在等待这个许可:false
有0个线程在等待
有线程在等待这个许可:false
有0个线程在等待
有线程在等待这个许可:false
有线程在等待这个许可:false
有0个线程在等待
有0个线程在等待
有线程在等待这个许可:false
有0个线程在等待
有线程在等待这个许可:false
有线程在等待这个许可:false
有2个线程在等待
有线程在等待这个许可:false
有0个线程在等待
有3个线程在等待
有2个线程在等待
线程0:开始执行,时间:2019-04-13 11:10:27.939
线程4:开始执行,时间:2019-04-13 11:10:27.939
线程5:开始执行,时间:2019-04-13 11:10:27.939
线程3:开始执行,时间:2019-04-13 11:10:27.939
线程0:执行结束,时间:2019-04-13 11:10:29.941
线程4:执行结束,时间:2019-04-13 11:10:29.941
线程5:执行结束,时间:2019-04-13 11:10:29.941
线程2:开始执行,时间:2019-04-13 11:10:29.941
线程8:开始执行,时间:2019-04-13 11:10:29.941
线程3:执行结束,时间:2019-04-13 11:10:29.941
线程9:开始执行,时间:2019-04-13 11:10:29.941
线程1:开始执行,时间:2019-04-13 11:10:29.941
线程2:执行结束,时间:2019-04-13 11:10:31.941
线程6:开始执行,时间:2019-04-13 11:10:31.941
线程8:执行结束,时间:2019-04-13 11:10:31.942
线程1:执行结束,时间:2019-04-13 11:10:31.942
线程7:开始执行,时间:2019-04-13 11:10:31.942
线程9:执行结束,时间:2019-04-13 11:10:31.942
线程6:执行结束,时间:2019-04-13 11:10:33.941
线程7:执行结束,时间:2019-04-13 11:10:33.942
`