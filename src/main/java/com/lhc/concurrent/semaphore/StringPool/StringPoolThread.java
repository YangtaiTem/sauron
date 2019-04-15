package com.lhc.concurrent.semaphore.StringPool;

public class StringPoolThread extends Thread{
    private StringPoolService stringPoolService;

    public StringPoolThread(StringPoolService stringPoolService) {
        super();
        this.stringPoolService = stringPoolService;
    }

    @Override
    public void run() {
        for(int i = 0; i < 64; i++){
            String s = stringPoolService.get();
            System.out.println(Thread.currentThread().getName() + " 取得值 " + s);
            stringPoolService.put(s);
        }
    }

    public static void main(String[] args){
        StringPoolService stringPoolService = new StringPoolService();
        StringPoolThread[] stringPoolThreads = new StringPoolThread[10];
        for(int i = 0; i < stringPoolThreads.length; i++){
            stringPoolThreads[i] = new StringPoolThread(stringPoolService);
        }
        for(int i = 0; i < stringPoolThreads.length; i++){
            stringPoolThreads[i].start();
        }
    }
}
