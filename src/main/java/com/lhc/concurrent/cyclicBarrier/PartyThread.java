package com.lhc.concurrent.cyclicBarrier;

public class PartyThread extends Thread {
    private PartyService partyService;

    public PartyThread(PartyService partyService) {
        super();
        this.partyService = partyService;
    }

    @Override
    public void run() {
        partyService.doService();
    }

    public static void main(String[] args) {
        PartyService partyService = new PartyService();
        PartyThread[] partyThreads = new PartyThread[3];
        for (int i = 0; i < partyThreads.length; i++) {
            partyThreads[i] = new PartyThread(partyService);
            partyThreads[i].setName(i + "");
            partyThreads[i].start();
        }
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("使用数" + partyService.cyclicBarrier.getParties());
        System.out.println("等待数" + partyService.cyclicBarrier.getNumberWaiting());
    }
}
