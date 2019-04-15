package com.lhc.concurrent.semaphore.acquire;

public class AcquireThread extends Thread{
    private AcquireService acquireService;

    public AcquireThread(AcquireService acquireService, String name) {
        super();
        this.acquireService = acquireService;
        this.setName(name);
    }

    public static void main(String[] args){
        AcquireService acquireService = new AcquireService();
        for (int i = 0; i < 10; i++) {
            AcquireThread acquireThread = new AcquireThread(acquireService, "线程" + i);
            acquireThread.start();
        }
    }




    @Override
    public void run() {
        acquireService.doSomething();
    }

    public AcquireService getAcquireService() {
        return acquireService;
    }

    public void setAcquireService(AcquireService acquireService) {
        this.acquireService = acquireService;
    }
}
