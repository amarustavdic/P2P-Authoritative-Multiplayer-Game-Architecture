package com.myproject.game.network.blockchain;

import com.google.gson.Gson;

public class Block {

    private int blockNumber;

    public Block(int blockNumber) {
        this.blockNumber = blockNumber;


    }



    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
