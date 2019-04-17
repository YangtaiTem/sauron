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
