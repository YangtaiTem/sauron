package com.lhc.concurrent.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PartyService {
    public CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
        @Override
        public void run() {
            System.out.println("彻底结束   " + System.currentTimeMillis());
        }
    });

    public void doService() {
        try {
            System.out.println(Thread.currentThread().getName() + "准备");
            if ("1".equals(Thread.currentThread().getName())) {
                Thread.sleep(Integer.MAX_VALUE);
            }
            cyclicBarrier.await(4, TimeUnit.SECONDS);
            System.out.println(Thread.currentThread().getName() + "结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }catch (TimeoutException e){
            e.printStackTrace();
        }
    }
}
