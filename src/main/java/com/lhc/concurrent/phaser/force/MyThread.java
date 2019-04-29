package com.lhc.concurrent.phaser.force;


import java.util.concurrent.Phaser;

public class MyThread extends Thread{
    private Phaser phaser;

    public MyThread(Phaser phaser) {
        super();
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser(3);
        MyThread myThread = new MyThread(phaser);
        myThread.setName("myThread");
        myThread.start();

        MyThread myThread1 = new MyThread(phaser);
        myThread1.setName("myThread1");
        myThread1.start();

        try{
            Thread.sleep(1000);
            //使屏障功能失效
            phaser.forceTermination();
            //判断是否销毁
            System.out.println(phaser.isTerminated());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
