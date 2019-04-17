/**
 * CountDownLatch 是一个同步功能的辅助类，使用效果是给定一个计数
 * 当使用这个 CountDownLatch 的线程判断技术不为0 时则当前线程呈wait状态
 * 为0则继续运行
 */
package com.lhc.concurrent.countDownLatch;

import java.util.concurrent.CountDownLatch;

public class MyThread extends Thread {
    private CountDownLatch comingTag;
    private CountDownLatch waitTag;
    private CountDownLatch waitRunTag;
    private CountDownLatch beingTag;
    private CountDownLatch endTag;

    public MyThread(CountDownLatch comingTag, CountDownLatch waitTag, CountDownLatch waitRunTag, CountDownLatch beingTag, CountDownLatch endTag) {
        super();
        this.comingTag = comingTag;
        this.waitTag = waitTag;
        this.waitRunTag = waitRunTag;
        this.beingTag = beingTag;
        this.endTag = endTag;
    }

    @Override
    public void run() {
        System.out.println("各位运动员正在到达起跑点的路上");
        try {
            /**
             * 各个线程运行使comingTag -1，
             * 然后waitTag 等待
             * 然后waitRunTag - 1
             * 然后beingTag 等待
             * 然后endTag - 1
             */
            Thread.sleep((int) Math.random() * 10000);
            System.out.println(Thread.currentThread().getName() + "到起跑点了");
            comingTag.countDown();
            System.out.println("等待裁判说准备");
            waitTag.await();
            System.out.println("准备起跑");
            Thread.sleep((int) (Math.random() * 10000 + 1000));
            waitRunTag.countDown();
            beingTag.await();
            System.out.println(Thread.currentThread().getName() + " 运动员开跑");
            Thread.sleep((int) (Math.random() * 10000 + 1000));
            endTag.countDown();
            System.out.println(Thread.currentThread().getName() + "运动员到达终点");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CountDownLatch comingTag = new CountDownLatch(10);
        CountDownLatch waitTag = new CountDownLatch(1);
        CountDownLatch waitRunTag = new CountDownLatch(10);
        CountDownLatch beingTag = new CountDownLatch(1);
        CountDownLatch endTag = new CountDownLatch(10);

        MyThread[] myThreads = new MyThread[10];
        for (int i = 0; i < myThreads.length; i++) {
            myThreads[i] = new MyThread(comingTag, waitTag, waitRunTag, beingTag, endTag);
            myThreads[i].start();
        }
        System.out.println("裁判员在等待选手的到来");
        try {
            /**
             * 与上面的run()方法结合起来看
             * comingTag 等待
             * waitTag -1
             * waitRunTag 等待
             * beingTag -1
             * endTag 等待
             */
            comingTag.await();
            System.out.println("所有运动员到来，裁判巡视五秒");
            Thread.sleep(5000);
            waitTag.countDown();
            System.out.println("各就各位");
            waitRunTag.await();
            Thread.sleep(2000);
            System.out.println("开始跑");
            beingTag.countDown();
            endTag.await();
            System.out.println("Game Over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
