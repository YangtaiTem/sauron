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
