package com.lhc.concurrent.phaser.arriveAndDeregister;

import java.util.concurrent.Phaser;

public class DerThread2 extends Thread{
    private Phaser phaser;

    public DerThread2(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        PhaserTool.method2();
    }
}
