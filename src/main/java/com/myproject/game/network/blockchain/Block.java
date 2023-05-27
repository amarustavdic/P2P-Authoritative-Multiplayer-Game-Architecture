package com.myproject.game.network.blockchain;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.SimpleTimeZone;

public class Block {

    private int blockNumber;
    private BigInteger modulo;
    private long timestamp;

    public Block(int blockNumber, BigInteger modulo) {
        this.blockNumber = blockNumber;
        this.modulo = modulo;
        this.timestamp = System.currentTimeMillis();
    }

    public Block(int blockNumber, BigInteger modulo, long timestamp) {
        this.blockNumber = blockNumber;
        this.modulo = modulo;
        this.timestamp = timestamp;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
