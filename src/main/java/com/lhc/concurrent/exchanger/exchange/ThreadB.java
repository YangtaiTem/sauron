package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class ThreadB extends Thread{
    private Exchanger<String> exchanger;

    public ThreadB(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            System.out.println("在线程B中得到线程A的值=" + exchanger.exchange("Chinese B"));
            System.out.println("B end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
