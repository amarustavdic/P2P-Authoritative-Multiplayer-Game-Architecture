package com.myproject.game.network.blockchain;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class BlockchainInbox {
    private final BlockingQueue<BlockchainMessage> outQueue;


    public BlockchainInbox() {
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
