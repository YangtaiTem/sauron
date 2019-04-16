/**
 * 实现生产者消费者模式
 * 限制生产者与消费者的数量
 */
package com.lhc.concurrent.semaphore.repast;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RepastService {
    /**
     * 初始化生产者和消费者两个信号量
     */
    volatile private Semaphore cook = new Semaphore(10);
    volatile private Semaphore diner = new Semaphore(5);
    /**
     * 初始化两个Condition
     */
    volatile private ReentrantLock lock = new ReentrantLock();
    volatile private Condition setCondition = lock.newCondition();
    volatile private Condition getCondition = lock.newCondition();
    /**
     * 初始化一个容器
     */
    volatile private Object[] producePosition = new Object[4];

    /**
     * 判断容器是否为空
     * @return
     */
    public boolean isEmpty() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断容器是否已满
     * @return
     */
    public boolean isFull() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在容器填一次数据
     * 当容器已满，就等待
     * 等到被通知再继续填充
     * 填充一次以后，通知未空
     */
    public void set() {
        try {
            cook.acquire();
            lock.lock();
            while (isFull()) {
                setCondition.await();
            }
            for (int i = 0; i < producePosition.length; i++) {
                if (producePosition[i] == null) {
                    producePosition[i] = "food";
                    System.out.println(Thread.currentThread().getName() + " cook " + producePosition[i] + i);
                    break;
                }
            }
            getCondition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cook.release();
        }
    }

    /**
     * 在容器取一次数据
     * 当容器已空时，就等待
     * 等到被通知再继续取
     * 取一次以后，通知未满
     */
    public void get() {
        try {
            diner.acquire();
            lock.lock();
            while (isEmpty()) {
                getCondition.await();
            }
            for (int i = 0; i < producePosition.length; i++) {
                if (producePosition[i] != null) {
                    System.out.println(Thread.currentThread().getName() + " eat food" + i);
                    producePosition[i] = null;
                    break;
                }
            }
            setCondition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            diner.release();
        }
    }
}
