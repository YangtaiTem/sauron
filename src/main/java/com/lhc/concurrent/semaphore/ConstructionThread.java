package com.lhc.concurrent.semaphore;

public class ConstructionThread extends Thread{
    private ConstructionService constructionService;

    public ConstructionThread(ConstructionService constructionService, String name) {
        super();
        this.constructionService = constructionService;
        this.setName(name);
    }

    public static void main(String[] args) {
        ConstructionService constructionService = new ConstructionService();
        for (int i = 0; i < 10; i++) {
            ConstructionThread semaphoreThread = new ConstructionThread(constructionService, "线程" + i);
            semaphoreThread.start();
        }
    }

    @Override
    public void run() {
        constructionService.doSomething();
    }
}
