package com.myproject.game.network.blockchain;

public class BlockchainMessage {
    private BlockchainMessageType type;
    private String payload;
    private String msgID;


    public BlockchainMessage(BlockchainMessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }


    public BlockchainMessageType getType() {
        return type;
    }
}
