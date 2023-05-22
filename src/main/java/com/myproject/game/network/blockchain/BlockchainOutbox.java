package com.myproject.game.network.blockchain;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class BlockchainOutbox {
    private final BlockingQueue<BlockchainMessage> outQueue;


    public BlockchainOutbox() {
        outQueue = new LinkedBlockingQueue<>();
    }



    public BlockchainMessage getNextMessage() throws InterruptedException {
        return outQueue.take();
    }

    public void addMessage(BlockchainMessage message) throws InterruptedException {
        outQueue.put(message);
    }

    public boolean isEmpty() {
        return outQueue.isEmpty();
    }
}
