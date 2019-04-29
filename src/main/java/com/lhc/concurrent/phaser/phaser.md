### Phaser 移相器 
> Phaser具有设置多屏障的功能。   

    
1.方法arriveAndAwaitAdvice()

    方法arriveAndAwaitAdvice()的作用与CountDownLatch中的await()方法大体一样。       
    另一个作用是计数不足时，线程呈阻塞状态，不能继续向下运行。
      
    
21.方法arriveAndDeregister()

    使当前线程退出，并且是parties值减1
    
22.代码

```

package com.lhc.concurrent.phaser.arriveAndDeregister;

import java.util.concurrent.Phaser;

public class DerThread1 extends Thread{
    private Phaser phaser;

    public DerThread1(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        PhaserTool.method1();
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser(3);
        PhaserTool.phaser = phaser;

        DerThread1 derThreadA = new DerThread1(phaser);
        derThreadA.setName("A");
        derThreadA.start();
        DerThread2 derThreadC = new DerThread2(phaser);
        derThreadC.setName("C");
        derThreadC.start();
    }
}

```

```

package com.lhc.concurrent.phaser.arriveAndDeregister;

import java.util.concurrent.Phaser;

public class DerThread2 extends Thread{
    private Phaser phaser;

    public DerThread2(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        PhaserTool.method2();
    }
}


```
    
    
```

package com.lhc.concurrent.phaser.arriveAndDeregister;

import com.lhc.concurrent.exchanger.timeout.ThreadA;

import java.util.concurrent.Phaser;

public class PhaserTool {
    public static Phaser phaser;

    public static void method1(){
        System.out.println(Thread.currentThread().getName() + " 1 begin = " + System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " 1 end = " + System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName() + " 2 begin = " + System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " 2 end = " + System.currentTimeMillis());
    }

    public static void method2(){
        try {
            System.out.println(Thread.currentThread().getName() + " 1 begin = " + System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " parties: " + phaser.getRegisteredParties());
            phaser.arriveAndDeregister();
            System.out.println(Thread.currentThread().getName() + " 取消注册," + "parties: " + phaser.getRegisteredParties());
            phaser.arriveAndDeregister();
            System.out.println(Thread.currentThread().getName() + " 取消注册," + "parties: " + phaser.getRegisteredParties());
            System.out.println(Thread.currentThread().getName() + " 1 end = " + System.currentTimeMillis());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}


```

23.输出结果

> A 1 begin = 1556525129659  
  C 1 begin = 1556525129659   
  C parties: 3   
  C 取消注册,parties: 2   
  C 取消注册,parties: 1   
  C 1 end = 1556525131661   
  A 1 end = 1556525131661   
  A 2 begin = 1556525131661   
  A 2 end = 1556525131661   
  
  
31.getPhaser()和onAdvance()方法   

    getPhaser() 获取的是已经到达第几个屏障
    onAdvance() 在通过新的屏障时被调用;返回true时，取消屏障
    
32.代码

```

package com.lhc.concurrent.phaser.onAdvice;

import java.util.concurrent.Phaser;

public class MyService {
    private Phaser phaser;

    public MyService(Phaser phaser) {
        this.phaser = phaser;
    }

    public void doSomething(){
        try{
            System.out.println("A begin, ThreadName = " + Thread.currentThread().getName() + System.currentTimeMillis());
            if("myThread2".equals(Thread.currentThread().getName())){
                Thread.sleep(4000);
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println("A end, ThreadName = " + Thread.currentThread().getName() + " ,end phaser value = " +
                    phaser.getPhase() + " " + System.currentTimeMillis());

            System.out.println("B begin, ThreadName = " + Thread.currentThread().getName() + System.currentTimeMillis());
            if("myThread2".equals(Thread.currentThread().getName())){
                Thread.sleep(4000);
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println("B end, ThreadName = " + Thread.currentThread().getName() + " ,end phaser value = " +
                    phaser.getPhase() + " " + System.currentTimeMillis());

            System.out.println("C begin, ThreadName = " + Thread.currentThread().getName() + System.currentTimeMillis());
            if("myThread2".equals(Thread.currentThread().getName())){
                Thread.sleep(4000);
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println("C end, ThreadName = " + Thread.currentThread().getName() + " ,end phaser value = " +
                    phaser.getPhase() + " " + System.currentTimeMillis());

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

```

33.测试类

```

package com.lhc.concurrent.phaser.onAdvice;

import java.util.concurrent.Phaser;

public class MyThread extends Thread{
    private MyService myService;

    public MyThread(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.doSomething();
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser(2){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("onAdvice() 方法被调用");
                return true;
            }
        };

        MyService myService = new MyService(phaser);

        MyThread myThread1 = new MyThread(myService);
        myThread1.setName("myThread1");
        MyThread myThread2 = new MyThread(myService);
        myThread2.setName("myThread2");
        myThread1.start();
        myThread2.start();
    }
}

```

33.输出结果

> A begin, ThreadName = myThread1 1556536238588    
  A begin, ThreadName = myThread2 1556536238588    
  onAdvice() 方法被调用   
  A end, ThreadName = myThread2 ,end phaser value = -2147483647 1556536242598   
  A end, ThreadName = myThread1 ,end phaser value = -2147483647 1556536242598   
  B begin, ThreadName = myThread1 1556536242598   
  B begin, ThreadName = myThread2 1556536242598    
  B end, ThreadName = myThread1 ,end phaser value = -2147483647 1556536242598   
  C begin, ThreadName = myThread1 1556536242598    
  C end, ThreadName = myThread1 ,end phaser value = -2147483647 1556536242598    
  B end, ThreadName = myThread2 ,end phaser value = -2147483647 1556536246610    
  C begin, ThreadName = myThread2 1556536246610    
  C end, ThreadName = myThread2 ,end phaser value = -2147483647 1556536250614     

41.arrive()方法

    使parties值加1，并且不在屏障处等待，直接向后面的逻辑继续运行，并且Phaser类有计数重置功能
    
42.代码

```

package com.lhc.concurrent.phaser.arrive;

import java.util.concurrent.Phaser;

public class MyService {
    public Phaser phaser;

    public MyService(Phaser phaser) {
        super();
        this.phaser = phaser;
    }

    public void testA() {
        try {
            System.out.println(Thread.currentThread().getName() + " begin a1 " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            phaser.arriveAndAwaitAdvance();
            System.out.println(Thread.currentThread().getName() + " end a1 " + System.currentTimeMillis());

            System.out.println(Thread.currentThread().getName() + " begin a2 " + System.currentTimeMillis());
            Thread.sleep(5000);
            phaser.arriveAndAwaitAdvance();
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            System.out.println(Thread.currentThread().getName() + " end a2 " + System.currentTimeMillis());

            System.out.println(Thread.currentThread().getName() + " begin a3 " + System.currentTimeMillis());
            Thread.sleep(5000);
            phaser.arriveAndAwaitAdvance();
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            System.out.println(Thread.currentThread().getName() + " end a3 " + System.currentTimeMillis());

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void testC() {
        try {
            System.out.println(Thread.currentThread().getName() + " begin c1 " + System.currentTimeMillis());
            Thread.sleep(10000);
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            phaser.arriveAndAwaitAdvance();
            System.out.println(Thread.currentThread().getName() + " end c1 " + System.currentTimeMillis());

            System.out.println(Thread.currentThread().getName() + " begin c2 " + System.currentTimeMillis());
            Thread.sleep(10000);
            phaser.arriveAndAwaitAdvance();
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            System.out.println(Thread.currentThread().getName() + " end c2 " + System.currentTimeMillis());

            System.out.println(Thread.currentThread().getName() + " begin c3 " + System.currentTimeMillis());
            Thread.sleep(10000);
            phaser.arriveAndAwaitAdvance();
            System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
            System.out.println(Thread.currentThread().getName() + " end c3 " + System.currentTimeMillis());

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void testB() {
        System.out.println(Thread.currentThread().getName() + " begin b1 " + System.currentTimeMillis());
        phaser.arrive();
        System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
        System.out.println(Thread.currentThread().getName() + " end b1 " + System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName() + " begin b2 " + System.currentTimeMillis());
        phaser.arrive();
        System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
        System.out.println(Thread.currentThread().getName() + " end b2 " + System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName() + " begin b3 " + System.currentTimeMillis());
        phaser.arrive();
        System.out.println("Parties:" + phaser.getArrivedParties() + ",Phase:" + phaser.getPhase());
        System.out.println(Thread.currentThread().getName() + " end b3 " + System.currentTimeMillis());
    }
}

```


43.测试

```

package com.lhc.concurrent.phaser.arrive;

import java.util.concurrent.Phaser;

public class MyThread extends Thread {
    private MyService myService;

    public MyThread(MyService myService, String name) {
        super();
        this.myService = myService;
        this.setName(name);
    }

    @Override
    public void run() {
        if (Thread.currentThread().getName().contains("A")) {
            myService.testA();
        } else if (Thread.currentThread().getName().contains("B")) {
            myService.testB();
        } else {
            myService.testC();
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        MyService myService = new MyService(phaser);

        MyThread a = new MyThread(myService, "A");
        a.start();
        MyThread c = new MyThread(myService, "C");
        c.start();
        MyThread b = new MyThread(myService, "B");
        b.start();
    }
}

```

44.输出结果

> A begin a1 1556537379472   
  B begin b1 1556537379472   
  C begin c1 1556537379472   
  Parties:1,Phase:0   
  B end b1 1556537379472   
  B begin b2 1556537379472   
  Parties:2,Phase:0   
  B end b2 1556537379472   
  B begin b3 1556537379472   
  Parties:0,Phase:1   
  B end b3 1556537379472   
  Parties:0,Phase:1   
  Parties:1,Phase:1   
  
  
51.awaitAdvance(int phase)

     作用：如果传入参数Phase和当前getPhase()方法返回值一样，就在屏障处等待，否则继续向下运行。
     
52.代码

```

package com.lhc.concurrent.phaser.awaitAdvance;

import java.util.concurrent.Phaser;

public class ThreadA extends Thread{
    private Phaser phaser;

    public ThreadA(Phaser phaser) {
        super();
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
    }
}

```

```

package com.lhc.concurrent.phaser.awaitAdvance;

import java.util.concurrent.Phaser;

public class ThreadB extends Thread{
    private Phaser phaser;

    public ThreadB(Phaser phaser) {
        super();
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
            Thread.sleep(3000);
            phaser.awaitAdvance(0);
            System.out.println(phaser.getPhase());
            System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

```

```

package com.lhc.concurrent.phaser.awaitAdvance;

import java.util.concurrent.Phaser;

public class ThreadC extends Thread{
    private Phaser phaser;

    public ThreadC(Phaser phaser) {
        super();
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
            Thread.sleep(5000);
            phaser.arriveAndAwaitAdvance();
            System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}


```

53.测试类

```

package com.lhc.concurrent.phaser.awaitAdvance;

import java.util.concurrent.Phaser;

public class Test {
    public static void main(String[] args){
        Phaser phaser = new Phaser(3);
        ThreadA a1 = new ThreadA(phaser);
        a1.setName("a1");
        a1.start();

        ThreadA a2 = new ThreadA(phaser);
        a2.setName("a2");
        a2.start();

        ThreadB b = new ThreadB(phaser);
        b.setName("b");
        b.start();

        ThreadC c = new ThreadC(phaser);
        c.setName("c");
        c.start();
    }
}

```

54.输出结果

>a1 begin 1556538438489   
 a2 begin 1556538438489   
 b begin 1556538438504   
 c begin 1556538438520   
 c end 1556538443535   
 a2 end 1556538443535   
 a1 end 1556538443535   
 1   
 b end 1556538443535   
 
61.方法forceTermination()和 方法isTerminated()

    方法forceTermination使Phaser对象的屏障功能失效
    方法isTerminated()判断Phaser对象是否已经呈销毁状态
    


    