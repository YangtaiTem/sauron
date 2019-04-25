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
