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
