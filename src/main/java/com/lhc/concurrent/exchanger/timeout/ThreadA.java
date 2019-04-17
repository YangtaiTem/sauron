package com.lhc.concurrent.exchanger.timeout;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ThreadA extends Thread{
    private Exchanger<String> exchanger;

    public ThreadA(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            System.out.println("在线程A中得到线程B的值=" + exchanger.exchange("Chinese A", 1, TimeUnit.SECONDS));
            System.out.println("A end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
