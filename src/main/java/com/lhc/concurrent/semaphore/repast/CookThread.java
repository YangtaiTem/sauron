package com.lhc.concurrent.semaphore.repast;

public class CookThread extends Thread {
    private RepastService repastService;

    public CookThread(RepastService repastService) {
        this.repastService = repastService;
    }

    @Override
    public void run() {
        repastService.set();
    }

    public static void main(String[] args) throws InterruptedException{
        RepastService repastService = new RepastService();
        CookThread[] cookThreads = new CookThread[20];
        EatThread[] eatThreads = new EatThread[20];

        for (int i = 0; i < 20; i++){
            cookThreads[i] = new CookThread(repastService);
            eatThreads[i] = new EatThread(repastService);
        }
        Thread.sleep(2000);
        for (int i = 0; i < 20; i++){
            cookThreads[i].start();
            eatThreads[i].start();
        }
    }
}
