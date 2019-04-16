package com.lhc.concurrent.semaphore.repast;

public class EatThread extends Thread {
    private RepastService repastService;

    public EatThread(RepastService repastService) {
        this.repastService = repastService;
    }

    @Override
    public void run() {
        repastService.get();
    }
}
