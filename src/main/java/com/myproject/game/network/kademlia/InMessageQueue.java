package com.myproject.game.network.kademlia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InMessageQueue {
    private final BlockingQueue<KademliaMessage> inQueue;

    public InMessageQueue() {
        this.inQueue = new LinkedBlockingQueue<>();
    }

    public KademliaMessage getNextMessage() {
        try {
            return inQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMessage(KademliaMessage message) {
        inQueue.offer(message);
    }

    public boolean isEmpty() {
        return inQueue.isEmpty();
    }

    public int length() {
        return inQueue.size();
    }
}
