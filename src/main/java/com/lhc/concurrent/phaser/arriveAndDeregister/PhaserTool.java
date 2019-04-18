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
