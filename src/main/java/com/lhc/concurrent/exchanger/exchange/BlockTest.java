package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class BlockTest {
    public static void main(String[] args){
        Exchanger<String> exchanger = new Exchanger<>();
        Thread a = new ThreadA(exchanger);
        Thread b = new ThreadB(exchanger);
        a.start();
        b.start();
        System.out.println("main end");
    }
}
