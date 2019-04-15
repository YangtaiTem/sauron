package com.lhc.concurrent.semaphore.many2many;

import java.util.concurrent.Semaphore;

public class MyService {
    private Semaphore semaphore = new Semaphore(3);

    public void doThing(){
        try {
            semaphore.acquire();
            System.out.println("ThreadName = " + Thread.currentThread().getName()
                    + "准备doThing:" + System.currentTimeMillis());
            for (int i = 0; i < 5; i ++){
                System.out.println("ThreadName = " + Thread.currentThread().getName()
                        + "打印" + (i + 1) + " " + System.currentTimeMillis());
            }
            System.out.println("doThing结束:" + System.currentTimeMillis());
            semaphore.release();
            System.out.println("ThreadName = " + Thread.currentThread().getName()
                    + "结束");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
