package com.lhc.concurrent.executorService.any;

import java.util.concurrent.Callable;

public class MyCallableA implements Callable<String>{

    @Override
    public String call() throws Exception {
        /**
         * 没有使用try-catch 进行捕获时，
         */
        System.out.println(Thread.currentThread().getName() + " begin ");
        for(int i = 0; i < 12345; i++){
            System.out.println("MyCallableA 运行中:" + i);
        }
        if(true) {
            System.out.println("中断了 ==============================");
            throw new NullPointerException();
        }
        System.out.println(Thread.currentThread().getName() + " end ");
        return "return A";

        /*try {
            System.out.println(Thread.currentThread().getName() + " begin ");
            for (int i = 0; i < 1234; i++) {
                System.out.println("MyCallableA 运行中:" + i);
            }
            if (true) {
                System.out.println("中断了 ==============================");
                throw new NullPointerException();
            }
            System.out.println(Thread.currentThread().getName() + " end ");
        }catch (Exception e){
            System.out.println(e.getMessage() + ":左边信息就是捕获的异常信息");
            throw e;
        }

        return "return A";*/
    }
}
