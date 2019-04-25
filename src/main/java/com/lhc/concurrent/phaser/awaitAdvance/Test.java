package com.lhc.concurrent.phaser.awaitAdvance;

import java.util.concurrent.Phaser;

public class Test {
    public static void main(String[] args){
        Phaser phaser = new Phaser(3);
        ThreadA a1 = new ThreadA(phaser);
        a1.setName("a1");
        a1.start();

        ThreadA a2 = new ThreadA(phaser);
        a2.setName("a2");
        a2.start();

        ThreadB b = new ThreadB(phaser);
        b.setName("b");
        b.start();

        ThreadC c = new ThreadC(phaser);
        c.setName("c");
        c.start();
    }
}
