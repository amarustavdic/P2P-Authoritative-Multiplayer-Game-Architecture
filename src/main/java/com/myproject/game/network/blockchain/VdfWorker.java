package com.myproject.game.network.blockchain;

import com.myproject.game.network.vdf.EvalResult;
import com.myproject.game.network.vdf.WesolowskiVDF;

public class VdfWorker implements Runnable {
    private WesolowskiVDF vdf;

    public VdfWorker(WesolowskiVDF vdf) {
        this.vdf = vdf;
    }



    @Override
    public void run() {
        int difficulty = 300000;
        long targetTime = 10000;  // 10 seconds
        long start;
        long end;
        long timeTaken;
        double deviation;

        while (true) {
            start = System.currentTimeMillis();
            EvalResult evalResult = vdf.eval("hello".getBytes(), difficulty, vdf.getN());
            end = System.currentTimeMillis();
            timeTaken = end - start;
            System.out.println("TIME TAKEN: " + timeTaken + " ms");

            deviation = ((double) (timeTaken - targetTime) / targetTime);
            System.out.println("Deviation: " + deviation);

            System.out.println(vdf.verify("hello".getBytes(), difficulty, evalResult.getLPrime(), evalResult.getProof()));

            difficulty = (int) (difficulty * deviation);
            System.out.println("New difficulty: " + difficulty);
        }
    }

}
