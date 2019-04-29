package com.lhc.concurrent.executor.before;

public class MyThread extends Thread{
    public MyThread(String name) {
        super();
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println("打印了! begin" + this.getName() + " " + System.currentTimeMillis());
        try {
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("打印了! end" + this.getName() + " " + System.currentTimeMillis());
    }
}
