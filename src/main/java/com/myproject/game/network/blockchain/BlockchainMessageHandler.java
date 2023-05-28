package com.myproject.game.network.blockchain;


import com.google.gson.Gson;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.Node;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class BlockchainMessageHandler implements Runnable {
    private final VDFService vdfService;
    private final KademliaDHT dht;
    private final BlockchainInbox inbox;
    private final BlockchainOutbox outbox;
    private final ArrayList<Block> chain;
    private final int maxRetries;
    private final int port;
    private final int connectionTimeout;
    private final Blockchain blockchain;
    private final InclusionRequestsList inclusionRequestsList;
    private final MatchRequestList matchRequestList;


    public BlockchainMessageHandler(MatchRequestList matchRequestList,InclusionRequestsList inclusionRequestsList, VDFService vdfService,KademliaDHT dht, BlockchainInbox inbox, BlockchainOutbox outbox, ArrayList<Block> chain, int maxRetries, int port, int connectionTimeout, Blockchain blockchain) {
        this.dht = dht;
        this.vdfService = vdfService;
        this.inbox = inbox;
        this.outbox = outbox;
        this.chain = chain;
        this.maxRetries = maxRetries;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.blockchain = blockchain;
        this.inclusionRequestsList = inclusionRequestsList;
        this.matchRequestList = matchRequestList;
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
                    addNewBlockToChain(message);
                    break;
                case INCLUSION_REQUEST:
                    System.out.println("inclusion request sent");
                    inclusionRequestsList.cacheNewInclusionRequest(message);
                    break;
                case RPS_MATCHMAKING_REQUEST:
                    matchRequestList.cacheRequest(message);
                    System.out.println("RPS request ... stored");
                    break;
                case TTT_MATCHMAKING_REQUEST:
                    matchRequestList.cacheRequest(message);
                    System.out.println("TTT request stored...");
                    break;
                default:
                    // for the rest doing nothing
                    break;
            }
        }
    }


    private void addNewBlockToChain(BlockchainMessage message) {
        Gson gson = new Gson();
        Block block = gson.fromJson(message.getPayload(), Block.class);
        blockchain.addNewBlock(block);
    }



    private void broadcastNewBlock(BlockchainMessage message) {
        Gson gson = new Gson();
        List<Node> knownPeers = dht.getKnowPeers(); // Use List instead of specific implementation

        Block block = gson.fromJson(message.getPayload(), Block.class);
        System.out.println("Block sequence number: " + block.getBlockNumber());

        // Check if the received block is the same as the last block in the chain
        if (!blockchain.getChain().isEmpty() && blockchain.getChain().get(blockchain.getChain().size() - 1).getBlockNumber() == block.getBlockNumber()) {
            return; // Avoid broadcasting the same block again
        }

        blockchain.addNewBlock(block); // Add the received block to the chain
        System.out.println(blockchain.getChain().size());
        blockchain.setNewBlock(true);
        System.out.println(blockchain.isNewBlock());




        for (Node peer : knownPeers) {
            int retries = 0;
            boolean messageSent = false;

            while (retries < maxRetries && !messageSent) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(peer.getAddress().getAddress(), port), connectionTimeout);
                    socket.getOutputStream().write(gson.toJson(message).getBytes());
                    socket.getOutputStream().flush();
                    messageSent = true; // Message sent successfully
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection timeout to: " + peer.getNodeId() + ". Retrying...");
                    retries++;
                } catch (IOException e) {
                    System.out.println("Failed to connect to: " + peer.getNodeId() + ". Retrying...");
                    retries++;
                }
            }

            if (!messageSent) {
                System.out.println("Unable to send message to: " + peer.getNodeId());
            }
        }
    }




}
