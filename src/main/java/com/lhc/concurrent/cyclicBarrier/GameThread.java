package com.lhc.concurrent.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

public class GameThread extends Thread {
    private GameService gameService;

    public GameThread(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void run() {
        gameService.game();
    }

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        GameService gameService = new GameService(cyclicBarrier);

        GameThread gameThread0 = new GameThread(gameService);
        gameThread0.setName("Thread0");
        gameThread0.start();
        GameThread gameThread1 = new GameThread(gameService);
        gameThread1.setName("Thread1");
        gameThread1.start();
        GameThread gameThread2 = new GameThread(gameService);
        gameThread2.setName("Thread2");
        gameThread2.start();
        GameThread gameThread3 = new GameThread(gameService);
        gameThread3.setName("Thread3");
        gameThread3.start();
    }
}
