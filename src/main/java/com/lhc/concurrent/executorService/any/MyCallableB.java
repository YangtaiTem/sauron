package com.lhc.concurrent.executorService.any;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyCallableB implements Callable<String> {
    @Override
    public String call() throws Exception {
        /*System.out.println(Thread.currentThread().getName() + " begin ");
        for (int i = 0; i < 321; i++) {
            System.out.println("MyCallableB 运行中:" + i);
        }
        System.out.println(Thread.currentThread().getName() + " end ");
        return "MyCallableB";*/


        try {
            System.out.println(Thread.currentThread().getName() + " begin ");
            for (int i = 0; i < 12345; i++) {
                System.out.println("MyCallableB 运行中:" + i);
            }
            if (true) {
                System.out.println(" ============================================== 中断了");
                throw new NullPointerException();
            }
            System.out.println(Thread.currentThread().getName() + " end ");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage() + "===========================================:左边信息就是捕获的异常信息");
            throw e;
        }
        return "MyCallableB";
    }

    public static void main(String[] args) {
        try {
            List callables = new ArrayList<>();
            callables.add(new MyCallableA());
            callables.add(new MyCallableB());
            ExecutorService service = Executors.newCachedThreadPool();
            String result= (String)service.invokeAny(callables);
            System.out.println("main取得的返回值 = " + result);
        }catch (ExecutionException e){
            System.out.println("main ExecutionException ");
            e.printStackTrace();
        }catch (InterruptedException e){
            System.out.println("main InterruptedException ");
            e.printStackTrace();
        }
    }
}
