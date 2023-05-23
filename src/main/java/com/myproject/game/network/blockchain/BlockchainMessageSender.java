package com.myproject.game.network.blockchain;

import com.google.gson.Gson;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.Node;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

public class BlockchainMessageSender implements Runnable {
    private final KademliaDHT dht;
    private final BlockchainOutbox outbox;
    private final Socket socket;
    private final int port;
    private final int connectionTimeout;
    private final int maxRetries;


    public BlockchainMessageSender(KademliaDHT dht, BlockchainOutbox outbox, int port, int connectionTimeout, int maxRetries) {
        this.dht = dht;
        this.outbox = outbox;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.maxRetries = maxRetries;
        this.socket = new Socket();
    }


    @Override
    public void run() {
        while (true) {
            BlockchainMessage message = null;
            try {
                message = outbox.getNextMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendBlockchainMessage(message);
        }
    }


    private void sendBlockchainMessage(BlockchainMessage message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        List<Node> peers = dht.getKnowPeers();
        for (Node peer : peers) {
            int retries = 0;
            boolean messageSent = false;
            while (retries < maxRetries && !messageSent) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(peer.getAddress().getAddress(), port), connectionTimeout);
                    socket.getOutputStream().write(jsonMessage.getBytes());
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
