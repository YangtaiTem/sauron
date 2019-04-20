package com.lhc.concurrent.phaser.arrive;

import java.util.concurrent.Phaser;

public class MyThread extends Thread {
    private MyService myService;

    public MyThread(MyService myService, String name) {
        super();
        this.myService = myService;
        this.setName(name);
    }

    @Override
    public void run() {
        if (Thread.currentThread().getName().contains("A")) {
            myService.testA();
        } else if (Thread.currentThread().getName().contains("B")) {
            myService.testB();
        } else {
            myService.testC();
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        MyService myService = new MyService(phaser);

        MyThread a = new MyThread(myService, "A");
        a.start();
        MyThread c = new MyThread(myService, "C");
        c.start();
        MyThread b = new MyThread(myService, "B");
        b.start();
    }
}
