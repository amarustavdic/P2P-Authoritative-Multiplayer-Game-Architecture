package com.myproject.game.network.blockchain;

import com.google.gson.Gson;


public class BlockchainMessage {
    private final BlockchainMessageType type;
    private final String payload;


    public BlockchainMessage(BlockchainMessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public BlockchainMessageType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
