package com.lhc.concurrent.semaphore;

public class SemaphoreThread extends Thread{
    private ConstructionService constructionService;

    public SemaphoreThread(ConstructionService constructionService, String name) {
        super();
        this.constructionService = constructionService;
        this.setName(name);
    }

    public static void main(String[] args) {
        ConstructionService constructionService = new ConstructionService();
        for (int i = 0; i < 10; i++) {
            SemaphoreThread semaphoreThread = new SemaphoreThread(constructionService, "线程" + i);
            semaphoreThread.start();
        }
    }

    @Override
    public void run() {
        constructionService.doSomething();
    }
}
