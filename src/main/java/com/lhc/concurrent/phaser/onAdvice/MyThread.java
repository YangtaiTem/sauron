package com.lhc.concurrent.phaser.onAdvice;

import java.util.concurrent.Phaser;

public class MyThread extends Thread{
    private MyService myService;

    public MyThread(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.doSomething();
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser(2){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("onAdvice() 方法被调用");
                return true;
            }
        };

        MyService myService = new MyService(phaser);

        MyThread myThread1 = new MyThread(myService);
        myThread1.setName("myThread1");
        MyThread myThread2 = new MyThread(myService);
        myThread2.setName("myThread2");
        myThread1.start();
        myThread2.start();
    }
}
