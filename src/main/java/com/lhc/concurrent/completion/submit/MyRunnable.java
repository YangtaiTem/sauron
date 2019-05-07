package com.lhc.concurrent.completion.submit;

import java.util.concurrent.*;

public class MyRunnable implements Runnable{
    private User user;

    public MyRunnable(User user) {
        super();
        this.user = user;
    }

    @Override
    public void run() {
        user.setName("jt");
        user.setPassword("pwd");
        System.out.println("running end");
    }

    public static void main(String[] args) throws Exception{
        User user = new User("name", "pwd");
        MyRunnable myRunnable = new MyRunnable(user);

        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorCompletionService completionService = new ExecutorCompletionService(executorService);
        Future<User> submit = completionService.submit(myRunnable, user);
        System.out.println(submit.get().getName() + " " + submit.get().getPassword());
    }
}
