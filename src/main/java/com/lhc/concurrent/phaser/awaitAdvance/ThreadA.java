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
