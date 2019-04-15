package com.lhc.concurrent.semaphore.tryAcquire;

public class TryAcquireThread extends Thread{
    private TryAcquireService tryAcquireService;

    public TryAcquireThread(TryAcquireService tryAcquireService, String name) {
        super();
        this.tryAcquireService = tryAcquireService;
        this.setName(name);
    }

    public static void main(String[] args){
        TryAcquireService tryAcquireService = new TryAcquireService();
        for (int i = 0; i < 10; i++) {
            TryAcquireThread tryAcquireThread = new TryAcquireThread(tryAcquireService, "线程" + i);
            tryAcquireThread.start();
        }
    }

    @Override
    public void run() {
        //tryAcquireService.doSomething();
        tryAcquireService.doThing();
    }
}
