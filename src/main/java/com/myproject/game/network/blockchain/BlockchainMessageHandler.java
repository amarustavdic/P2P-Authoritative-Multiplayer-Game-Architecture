package com.myproject.game.network.blockchain;


import com.google.gson.Gson;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.Node;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class BlockchainMessageHandler implements Runnable {
    private final KademliaDHT dht;
    private final BlockchainInbox inbox;
    private final BlockchainOutbox outbox;
    private final ArrayList<Block> chain;
    private final int maxRetries;
    private final int port;
    private final int connectionTimeout;
    private final Blockchain blockchain;


    public BlockchainMessageHandler(KademliaDHT dht, BlockchainInbox inbox, BlockchainOutbox outbox, ArrayList<Block> chain, int maxRetries, int port, int connectionTimeout, Blockchain blockchain) {
        this.dht = dht;
        this.inbox = inbox;
        this.outbox = outbox;
        this.chain = chain;
        this.maxRetries = maxRetries;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.blockchain = blockchain;
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
                    System.out.println(new Gson().toJson(message));
                    System.out.println(new Gson().toJson(chain));
                    break;
                case NEW_BLOCK:
                    System.out.println("I have received new block");
                    //System.out.println(message.toJson());
                    broadcastNewBlock(message);
                    break;
            }
        }
    }



    private void broadcastNewBlock(BlockchainMessage message) {
        Gson gson = new Gson();
        ArrayList<Node> knownPeers = (ArrayList<Node>) dht.getKnowPeers();
        Block block = gson.fromJson(message.getPayload(), Block.class);
        System.out.print(" block sequence number: " + block.getBlockNumber());
        System.out.println();

        // new received block added / new last block
        blockchain.getChain().add(block);

        // resolivng broadcast looping
        if (chain.get(chain.size()-1).getBlockNumber() == block.getBlockNumber()) return;

        // set new block flag to true
        blockchain.setNewBlock(true);


        for (int i = 0; i < knownPeers.size(); i++) {
            int retries = 0;
            boolean messageSent = false;
            while (retries < maxRetries && !messageSent) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(knownPeers.get(i).getAddress().getAddress(), port), connectionTimeout);
                    socket.getOutputStream().write(gson.toJson(message).getBytes());
                    socket.getOutputStream().flush();
                    messageSent = true; // Message sent successfully
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection timeout to: " + knownPeers.get(i).getNodeId() + ". Retrying...");
                    retries++;
                } catch (IOException e) {
                    System.out.println("Failed to connect to: " + knownPeers.get(i).getNodeId() + ". Retrying...");
                    retries++;
                }
            }

            if (!messageSent) {
                System.out.println("Unable to send message to: " + knownPeers.get(i).getNodeId());
            }
        }
    }



}
