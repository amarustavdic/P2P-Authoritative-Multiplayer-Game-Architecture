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

        while (true) {

        }
    }
}
