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

>线程0:开始执行,时间:2019-04-13 10:08:44.290
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


> 有线程在等待这个许可:false
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



21.一些其他方法

>availablePermits()方法，表示返回Semaphore对象中的当前可用许可数，此方法通常用于调试，因为许可数量（通路）可能是实时在改变的。
drainPermits()方法可获取并返回立即可用的所有许可（通路）个数，并将可用许可置为0。


>getQueueLength()获取等待许可的线程个数。
hasQueuedThreads()判断有没有线程在等待这个许可。
getQueueLength()和hasQueuedThreads()通常都是在判断当前有没有等待许可的线程信息时使用

22.公平与非公平信号量
    
    有些时候，获取许可的顺序与线程启动的顺序有关，这时信号量就要分为公平与非公平的。
    公平信号量是获得所得顺序和线程启动顺序有关，但仅仅是在概率上，不代表 100%能获取。
    信号量默认的构造方法是创建非公平信号量(Semaphore semaphore = new Semaphore(8);)。
    创建公平信号量的方式是:
        Semaphore semaphore = new Semaphore(8, true);
        
23.tryAcquire()的使用

参数使用

    当前时刻
        
        tryAcquire(int permits)
        Acquires the given number of permits from this semaphore, only if all are available at the time of invocation.
        尝试去从这个信号量获取指定数量的在调用时都是可用的许可 。
        如果不使用permits 参数，tryAcquire()表示获取一个许可。

    指定时间
        
        tryAcquire(int permits, long timeout, TimeUnit unit)
        Acquires the given number of permits from this semaphore,
        if all become available within the given waiting time and the current thread has not been interrupted.
        在指定的时间内尝试去从这个信号量获取指定数量的许可 ，同时这段时间内，这个线程没有被中断。
        如果不使用permits 参数，tryAcquire(long timeout, TimeUnit unit)表示获取一个许可。  
   
代码 

```

package com.lhc.concurrent.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TryAcquireService {
    private Semaphore semaphore = new Semaphore(8);

    public void doSomething() {
        try {
            /**
             * tryAcquire(int permits)
             * Acquires the given number of permits from this semaphore, only if all are available at the time of invocation.
             * 尝试去从这个信号量获取指定数量的在调用时都是可用的许可 。
             * 如果不使用permits 参数，tryAcquire()表示获取一个许可。
             */
            if (semaphore.tryAcquire(2)) {
                System.out.println(Thread.currentThread().getName() + "获得锁,时间:" + System.currentTimeMillis());
                Thread.sleep(100);
                semaphore.release(2);
            }else {
                System.out.println(Thread.currentThread().getName() + "没有获得锁,时间:" + System.currentTimeMillis());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void doThing() {
        try {
            /**
             * tryAcquire(int permits, long timeout, TimeUnit unit)
             * Acquires the given number of permits from this semaphore,
             * if all become available within the given waiting time and the current thread has not been interrupted.
             * 在指定的时间内尝试去从这个信号量获取指定数量的许可 ，同时这段时间内，这个线程没有被中断。
             * 如果不使用permits 参数，tryAcquire(long timeout, TimeUnit unit)表示获取一个许可。
             */
            if (semaphore.tryAcquire(2, 1, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + "获得锁,时间:" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "释放锁,时间:" + System.currentTimeMillis());
                semaphore.release(2);
            }else {
                System.out.println(Thread.currentThread().getName() + "没有获得锁,时间:" + System.currentTimeMillis());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

```

测试类

```

package com.lhc.concurrent.semaphore;

public class TryAcquireThread extends Thread{
    private TryAcquireService tryAcquireService;

    public TryAcquireThread(TryAcquireService tryAcquireService, String name) {
        super();
        this.tryAcquireService = tryAcquireService;
        this.setName(name);
    }

    public static void main(String[] args){
        TryAcquireService tryAcquireService = new TryAcquireService();
        for (int i = 0; i < 10; i++) {
            TryAcquireThread tryAcquireThread = new TryAcquireThread(tryAcquireService, "线程" + i);
            tryAcquireThread.start();
        }
    }

    @Override
    public void run() {
        //tryAcquireService.doSomething();
        tryAcquireService.doThing();
    }
}


```

测试结果

获取当前时刻
> 线程0获得锁,时间:1555145916044
线程4没有获得锁,时间:1555145916044
线程2获得锁,时间:1555145916044
线程1获得锁,时间:1555145916044
线程6没有获得锁,时间:1555145916044
线程5没有获得锁,时间:1555145916044
线程3获得锁,时间:1555145916044
线程7没有获得锁,时间:1555145916044
线程8没有获得锁,时间:1555145916044
线程9没有获得锁,时间:1555145916044

获取指定时间内

>线程9获得锁,时间:1555146046722
线程7获得锁,时间:1555146046722
线程1获得锁,时间:1555146046722
线程6获得锁,时间:1555146046722
线程6释放锁,时间:1555146047722
线程9释放锁,时间:1555146047722
线程5获得锁,时间:1555146047722
线程4获得锁,时间:1555146047722
线程3没有获得锁,时间:1555146047722
线程1释放锁,时间:1555146047722
线程7释放锁,时间:1555146047722
线程0没有获得锁,时间:1555146047722
线程8没有获得锁,时间:1555146047722
线程2没有获得锁,时间:1555146047722
线程5释放锁,时间:1555146048723
线程4释放锁,时间:1555146048723


31.字符串池
>   类SemaPhore可以有效地对并发执行任务的线程数量进行限制，可以用在pool池技术中，
可以设置同时访问pool池中数据的线程数量。
    目的:实现同时有若干个线程可以访问池中的数据，但同时只有一个线程可以取得数据，使用后再放回。
    
代码:

```

package com.lhc.concurrent.semaphore.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StringPoolService {
    private int poolMaxSize = 3;
    //同时可以有5个线程访问这个pool
    private int semaphorePermits = 5;

    private Semaphore semaphore = new Semaphore(semaphorePermits);
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private List<String> list = new ArrayList<>();

    public StringPoolService() {
        super();
        //初始化一个list
        for (int i = 0; i < poolMaxSize; i++){
            list.add("String " + i);
        }
    }

    public String get(){
        String getString = null;
        try {
            semaphore.acquire();
            //加锁，能保证这段代码同一时刻只有一个线程在运行
            lock.lock();
            while (list.size() == 0){
                //如果pool为空，等待
                condition.await();
            }
            getString = list.remove(0);
            //解锁
            lock.unlock();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return getString;
    }

    public void put(String str){
        //加锁
        lock.lock();
        list.add(str);
        //填充以后通知，get方法就不再await
        condition.signalAll();
        //解锁
        lock.unlock();
        semaphore.release();
    }
}

```

测试类

```

/**
 * 类Semaphore 可以有效地对并发执行任务的线程数量进行限制
 * 创建一个字符串池，，同时有若干个线程可以访问池中的数据，但同时只有一个线程可以取得数据，使用完毕再放回
 */
package com.lhc.concurrent.semaphore.StringPool;

public class StringPoolThread extends Thread {
    private StringPoolService stringPoolService;

    public StringPoolThread(StringPoolService stringPoolService) {
        super();
        this.stringPoolService = stringPoolService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 64; i++) {
            //取了再放进去
            String s = stringPoolService.get();
            System.out.println(Thread.currentThread().getName() + " 取得值 " + s);
            stringPoolService.put(s);
        }
    }

    public static void main(String[] args) {
        StringPoolService stringPoolService = new StringPoolService();
        StringPoolThread[] stringPoolThreads = new StringPoolThread[10];
        for (int i = 0; i < stringPoolThreads.length; i++) {
            stringPoolThreads[i] = new StringPoolThread(stringPoolService);
        }
        for (int i = 0; i < stringPoolThreads.length; i++) {
            stringPoolThreads[i].start();
        }
    }
}


```

测试结果

> Thread-1 取得值 String 1
  Thread-0 取得值 String 0
  Thread-2 取得值 String 2
  Thread-7 取得值 String 1
  Thread-2 取得值 String 0
  Thread-1 取得值 String 1
  Thread-7 取得值 String 2
  Thread-3 取得值 String 0
  Thread-1 取得值 String 1
  Thread-3 取得值 String 2
  Thread-2 取得值 String 0
  Thread-7 取得值 String 2
  Thread-1 取得值 String 1
  Thread-7 取得值 String 2
  Thread-2 取得值 String 0
  Thread-7 取得值 String 2
  Thread-1 取得值 String 1
  Thread-3 取得值 String 2
  Thread-2 取得值 String 0
  Thread-3 取得值 String 2
  Thread-5 取得值 String 1
  Thread-4 取得值 String 2
  Thread-3 取得值 String 0
  Thread-4 取得值 String 2
  Thread-5 取得值 String 1
  Thread-4 取得值 String 2
  Thread-8 取得值 String 0
  Thread-4 取得值 String 2
  Thread-6 取得值 String 1
  Thread-0 取得值 String 2
  Thread-8 取得值 String 0
  Thread-0 取得值 String 2
  Thread-6 取得值 String 1
  Thread-0 取得值 String 2
  Thread-8 取得值 String 0
  Thread-0 取得值 String 2
  Thread-6 取得值 String 1
  Thread-4 取得值 String 2
  Thread-8 取得值 String 0
  Thread-9 取得值 String 2
  Thread-6 取得值 String 1
  Thread-9 取得值 String 2
  Thread-8 取得值 String 0
  Thread-9 取得值 String 2
  Thread-6 取得值 String 1
  Thread-9 取得值 String 2
  Thread-5 取得值 String 0
  Thread-9 取得值 String 1
  Thread-5 取得值 String 2
  Thread-5 取得值 String 0
  
  
32.多生产者/消费者

> 多个生产者和消费者，同时限制生产者和消费者的数量
类Semaphore提供了限制并发线程数的功能，此功能在默认的synchronized中是不提供的。

代码

```

/**
 * 实现生产者消费者模式
 * 限制生产者与消费者的数量
 */
package com.lhc.concurrent.semaphore.repast;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RepastService {
    /**
     * 初始化生产者和消费者两个信号量
     */
    volatile private Semaphore cook = new Semaphore(10);
    volatile private Semaphore diner = new Semaphore(5);
    /**
     * 初始化两个Condition
     */
    volatile private ReentrantLock lock = new ReentrantLock();
    volatile private Condition setCondition = lock.newCondition();
    volatile private Condition getCondition = lock.newCondition();
    /**
     * 初始化一个容器
     */
    volatile private Object[] producePosition = new Object[4];

    /**
     * 判断容器是否为空
     * @return
     */
    public boolean isEmpty() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断容器是否已满
     * @return
     */
    public boolean isFull() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在容器填一次数据
     * 当容器已满，就等待
     * 等到被通知再继续填充
     * 填充一次以后，通知未空
     */
    public void set() {
        try {
            cook.acquire();
            lock.lock();
            while (isFull()) {
                setCondition.await();
            }
            for (int i = 0; i < producePosition.length; i++) {
                if (producePosition[i] == null) {
                    producePosition[i] = "food";
                    System.out.println(Thread.currentThread().getName() + " cook " + producePosition[i] + i);
                    break;
                }
            }
            getCondition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cook.release();
        }
    }

    /**
     * 在容器取一次数据
     * 当容器已空时，就等待
     * 等到被通知再继续取
     * 取一次以后，通知未满
     */
    public void get() {
        try {
            diner.acquire();
            lock.lock();
            while (isEmpty()) {
                getCondition.await();
            }
            for (int i = 0; i < producePosition.length; i++) {
                if (producePosition[i] != null) {
                    System.out.println(Thread.currentThread().getName() + " eat food" + i);
                    producePosition[i] = null;
                    break;
                }
            }
            setCondition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            diner.release();
        }
    }
}


```

测试类

```

package com.lhc.concurrent.semaphore.repast;

public class CookThread extends Thread {
    private RepastService repastService;

    public CookThread(RepastService repastService) {
        this.repastService = repastService;
    }

    @Override
    public void run() {
        repastService.set();
    }

    public static void main(String[] args) throws InterruptedException{
        RepastService repastService = new RepastService();
        CookThread[] cookThreads = new CookThread[20];
        EatThread[] eatThreads = new EatThread[20];

        for (int i = 0; i < 20; i++){
            cookThreads[i] = new CookThread(repastService);
            eatThreads[i] = new EatThread(repastService);
        }
        Thread.sleep(2000);
        for (int i = 0; i < 20; i++){
            cookThreads[i].start();
            eatThreads[i].start();
        }
    }
}


package com.lhc.concurrent.semaphore.repast;

public class EatThread extends Thread {
    private RepastService repastService;

    public EatThread(RepastService repastService) {
        this.repastService = repastService;
    }

    @Override
    public void run() {
        repastService.get();
    }
}


```

测试结果

> Thread-0 cook food0
  Thread-11 eat food0
  Thread-8 cook food0
  Thread-4 cook food1
  Thread-2 cook food2
  Thread-3 eat food0
  Thread-16 cook food0
  Thread-18 cook food3
  Thread-5 eat food0
  Thread-26 cook food0
  Thread-1 eat food0
  Thread-28 cook food0
  Thread-15 eat food0
  Thread-6 cook food0
  Thread-9 eat food0
  Thread-19 eat food1
  Thread-7 eat food2
  Thread-38 cook food0
  Thread-12 cook food1
  Thread-14 cook food2
  Thread-13 eat food0
  Thread-29 eat food1
  Thread-30 cook food0
  Thread-24 cook food1
  Thread-17 eat food0
  Thread-25 eat food1
  Thread-36 cook food0
  Thread-21 eat food0
  Thread-23 eat food2
  Thread-37 eat food3
  Thread-20 cook food0
  Thread-32 cook food1
  Thread-22 cook food2
  Thread-34 cook food3
  Thread-31 eat food0
  Thread-27 eat food1
  Thread-10 cook food0
  Thread-33 eat food0
  Thread-35 eat food2
  Thread-39 eat food3