/**
 * 类Semaphore 可以有效地对并发执行任务的线程数量进行限制
 * 创建一个字符串池，，同时有若干个线程可以访问池中的数据，但同时只有一个线程可以取得数据，使用完毕再放回
 */
package com.lhc.concurrent.semaphore.StringPool;

public class StringPoolThread extends Thread {
    private StringPoolService stringPoolService;

    public StringPoolThread(StringPoolService stringPoolService) {
        super();
        this.stringPoolService = stringPoolService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            //取了再放进去
            String s = stringPoolService.get();
            System.out.println(Thread.currentThread().getName() + " 取得值 " + s);
            stringPoolService.put(s);
        }
    }

    public static void main(String[] args) {
        StringPoolService stringPoolService = new StringPoolService();
        StringPoolThread[] stringPoolThreads = new StringPoolThread[10];
        for (int i = 0; i < stringPoolThreads.length; i++) {
            stringPoolThreads[i] = new StringPoolThread(stringPoolService);
        }
        for (int i = 0; i < stringPoolThreads.length; i++) {
            stringPoolThreads[i].start();
        }
    }
}
