package com.lhc.concurrent.completion.nonBlock;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.concurrent.*;

public class MyCallable implements Callable<String>{
    private String userName;
    private long sleepTime;

    public MyCallable(String userName, long sleepTime) {
        this.userName = userName;
        this.sleepTime = sleepTime;
    }

    @Override
    public String call() throws Exception {
        System.out.println(userName);
        Thread.sleep(sleepTime);
        return "return " + sleepTime;
    }

    public static void main(String[] args) throws Exception{
        MyCallable name1 = new MyCallable("name1", 1050);
        MyCallable name2 = new MyCallable("name2", 1040);
        MyCallable name3 = new MyCallable("name3", 1030);
        MyCallable name4 = new MyCallable("name4", 1020);
        MyCallable name5 = new MyCallable("name5", 1010);

        ArrayList<Callable> callables = Lists.newArrayList();
        callables.add(name1);
        callables.add(name2);
        callables.add(name3);
        callables.add(name4);
        callables.add(name5);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 5,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        ExecutorCompletionService completionService = new ExecutorCompletionService(executor);

        for (int i = 0; i < callables.size(); i++) {
            completionService.submit(callables.get(i));
        }

        for (int i = 0; i < 5; i++){
            /**
             * 哪个任务先执行完，那个任务的返回值就先打印，解决了Future 阻塞的特点
             * 但是如果没有任务被执行完，则.take().get()方法还是呈阻塞特性。
             */
            System.out.println(completionService.take().get());
        }
    }
}
