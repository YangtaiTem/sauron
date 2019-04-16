package com.lhc.concurrent.semaphore.repast;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RepastService {
    volatile private Semaphore cook = new Semaphore(10);
    volatile private Semaphore diner = new Semaphore(5);
    volatile private ReentrantLock lock = new ReentrantLock();
    volatile private Condition setCondition = lock.newCondition();
    volatile private Condition getCondition = lock.newCondition();
    volatile private Object[] producePosition = new Object[4];

    public boolean isEmpty() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < producePosition.length; i++) {
            if (producePosition[i] == null) {
                return false;
            }
        }
        return true;
    }

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
