package com.lhc.concurrent.executorService.all;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableA implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
        return "return A";
    }

    public static void main(String[] args) {
        CallableA callableA = new CallableA();
        CallableB callableB = new CallableB();

        ArrayList<Callable<String>> callables = new ArrayList<>();
        callables.add(callableA);
        callables.add(callableB);
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("invokeAll Begin " + System.currentTimeMillis());
            List<Future<String>> futures = executorService.invokeAll(callables);
            System.out.println("invokeAll end " + System.currentTimeMillis());
            for (int i = 0; i < futures.size(); i++) {
                System.out.println("返回的结果 = " + futures.get(i).get() + " " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
