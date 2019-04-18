package com.lhc.concurrent.phaser.arriveAndDeregister;

import java.util.concurrent.Phaser;

public class DerThread1 extends Thread{
    private Phaser phaser;

    public DerThread1(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        PhaserTool.method1();
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser(3);
        PhaserTool.phaser = phaser;

        DerThread1 derThreadA = new DerThread1(phaser);
        derThreadA.setName("A");
        derThreadA.start();
       /* DerThread1 derThreadB = new DerThread1(phaser);
        derThreadB.setName("B");
        derThreadB.start();*/
        DerThread2 derThreadC = new DerThread2(phaser);
        derThreadC.setName("C");
        derThreadC.start();
    }
}
