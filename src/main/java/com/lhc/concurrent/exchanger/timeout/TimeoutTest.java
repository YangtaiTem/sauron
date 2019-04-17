package com.lhc.concurrent.exchanger.timeout;


import java.util.concurrent.Exchanger;

public class TimeoutTest {
    public static void main(String[] args){
        Exchanger<String> exchanger = new Exchanger<>();
        ThreadA a = new ThreadA(exchanger);
        a.start();
        System.out.println("main end");
    }

}
