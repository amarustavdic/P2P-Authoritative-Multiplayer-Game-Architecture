package com.myproject.game.network.kademlia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class OutMessageQueue {
    private final BlockingQueue<KademliaMessage> outQueue;

    public OutMessageQueue() {
        outQueue = new LinkedBlockingQueue<>();
    }

    public KademliaMessage getNextMessage() throws InterruptedException {
        return outQueue.take();
    }

    public void addMessage(KademliaMessage message) throws InterruptedException {
        outQueue.put(message);
    }

    public boolean isEmpty() {
        return outQueue.isEmpty();
    }
}
