package com.myproject.game.network.blockchain;

import com.google.gson.Gson;

import java.math.BigInteger;

public class Block {

    private int blockNumber;
    private BigInteger modulo;

    public Block(int blockNumber, BigInteger modulo) {
        this.blockNumber = blockNumber;
        this.modulo = modulo;
    }



    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
