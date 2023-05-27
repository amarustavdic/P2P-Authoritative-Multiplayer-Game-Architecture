package com.myproject.game.network.blockchain;


import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.RoutingTable;

import static com.myproject.game.network.blockchain.BlockchainMessageType.SYNC;


public class BlockchainMessageHandler implements Runnable {
    private final KademliaDHT dht;
    private final BlockchainInbox inbox;
    private final BlockchainOutbox outbox;


    public BlockchainMessageHandler(KademliaDHT dht, BlockchainInbox inbox, BlockchainOutbox outbox) {
        this.dht = dht;
        this.inbox = inbox;
        this.outbox = outbox;
    }


    @Override
    public void run() {
        BlockchainMessage message = null;
        while (true) {
            try {
                message = inbox.getNextMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (message.getType()) {
                case SYNC:
                    System.out.println("I have received sync request!");

                    break;
            }
        }
    }




}
