package com.lhc.concurrent.semaphore.many2one;

public class MyThread extends Thread{
    private MyService myService;

    public MyThread(MyService myService) {
        super();
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.doThing();
    }

    public static void main(String[] args){
        MyService myService = new MyService();

        MyThread[] myThreads = new MyThread[12];
        for (int i = 0; i < myThreads.length; i++){
            myThreads[i] = new MyThread(myService);
            myThreads[i].start();
        }
    }
}
