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
