package com.myproject.game.network.kademlia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OutMessageQueue {

    private final BlockingQueue<KademliaMessage> outQueue;


    public OutMessageQueue() {
        this.outQueue = new LinkedBlockingQueue<>();
    }


    public KademliaMessage getNextMessage() {
        KademliaMessage nextMessage = null;
        try {
            nextMessage = outQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nextMessage;
    }

    public void addMessage(KademliaMessage message) {
        try {
            outQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return outQueue.isEmpty();
    }
}
