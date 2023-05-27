package com.myproject.game.network.blockchain;

import com.myproject.game.network.vdf.EvalResult;
import com.myproject.game.network.vdf.WesolowskiVDF;

import java.util.ArrayList;

public class VdfWorker implements Runnable {
    private WesolowskiVDF vdf;

    public VdfWorker(WesolowskiVDF vdf, ArrayList<Block> chain) {
        this.vdf = vdf;
    }



    @Override
    public void run() {

        while (true) {

        }
    }
}
