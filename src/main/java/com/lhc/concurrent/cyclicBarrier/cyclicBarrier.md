###   CyclicBarrier 的使用

1.介绍

> 可以实现屏障等待的功能（阶段性同步），同时可以循环实现要一起做任务的目标。   
CyclicBarrier是一个同步辅助类，它允许一组线程互相等待，直到某个公告屏障点，这些线程必须相互等待。

2.CyclicBarrier 和 CountDownLatch 的区别:
> CountDownLatch:一个或多个线程，等待另外一个或多个线程完成某个事情之后才能继续执行。   
CyclicBarrier:多个线程时间相互等待，任何一个线程完成之前，所有的线程都必须等待。    

3.实现阶段跑步比赛

代码

```

package com.lhc.concurrent.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GameService {
    private CyclicBarrier cyclicBarrier;

    public GameService(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    public void game() {
        try {
            Thread.sleep((int) (Math.random() * 10000));
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() +
                    "开始跑第1阶段 " + (cyclicBarrier.getNumberWaiting() + 1));
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() +
                    "结束跑第1阶段 " + (cyclicBarrier.getNumberWaiting()));

            Thread.sleep((int) (Math.random() * 10000));
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() +
                    "开始跑第2阶段 " + (cyclicBarrier.getNumberWaiting() + 1));
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() +
                    "结束跑第2阶段 " + (cyclicBarrier.getNumberWaiting()));
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException b) {
            b.printStackTrace();
        }
    }
}


```

测试类

```

package com.lhc.concurrent.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

public class GameThread extends Thread {
    private GameService gameService;

    public GameThread(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void run() {
        gameService.game();
    }

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        GameService gameService = new GameService(cyclicBarrier);

        GameThread gameThread0 = new GameThread(gameService);
        gameThread0.setName("Thread0");
        gameThread0.start();
        GameThread gameThread1 = new GameThread(gameService);
        gameThread1.setName("Thread1");
        gameThread1.start();
        GameThread gameThread2 = new GameThread(gameService);
        gameThread2.setName("Thread2");
        gameThread2.start();
        GameThread gameThread3 = new GameThread(gameService);
        gameThread3.setName("Thread3");
        gameThread3.start();
    }
}

```

打印结果

> Thread1 1555921960659开始跑第1阶段 1   
  Thread2 1555921964154开始跑第1阶段 2   
  Thread2 1555921964154结束跑第1阶段 0   
  Thread1 1555921964154结束跑第1阶段 0   
  Thread0 1555921964821开始跑第1阶段 1   
  Thread2 1555921967049开始跑第2阶段 2   
  Thread2 1555921967049结束跑第2阶段 0   
  Thread0 1555921967049结束跑第1阶段 0   
  Thread3 1555921968773开始跑第1阶段 1   
  Thread1 1555921973957开始跑第2阶段 2   
  Thread1 1555921973957结束跑第2阶段 0   
  Thread3 1555921973957结束跑第1阶段 0   
  Thread0 1555921976853开始跑第2阶段 1   
  Thread3 1555921977629开始跑第2阶段 2   
  Thread3 1555921977629结束跑第2阶段 0   
  Thread0 1555921977629结束跑第2阶段 0   