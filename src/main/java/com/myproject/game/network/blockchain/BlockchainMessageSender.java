package com.myproject.game.network.blockchain;

import com.google.gson.Gson;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.Node;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class BlockchainMessageSender implements Runnable {
    private final Gson gson;
    private final KademliaDHT dht;
    private final BlockchainOutbox outbox;
    private final int port;
    private final int connectionTimeout;
    private final int maxRetries;


    public BlockchainMessageSender(KademliaDHT dht, BlockchainOutbox outbox, int port, int connectionTimeout, int maxRetries) {
        this.gson = new Gson();
        this.dht = dht;
        this.outbox = outbox;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.maxRetries = maxRetries;

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

            switch (message.getType()) {
                case SYNC:
                    sendSyncRequest(message);
                    break;
                case NEW_BLOCK:
                    broadcastNewBlock(message);
                    System.out.println("new block broadcast");
                    break;
            }
        }
    }


    private void broadcastNewBlock(BlockchainMessage message) {
        ArrayList<Node> knownPeers = (ArrayList<Node>) dht.getKnowPeers();

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



    private void sendSyncRequest(BlockchainMessage message) {
        Node receiver = dht.getClosestPeer();

        int retries = 0;
        boolean messageSent = false;
        while (retries < maxRetries && !messageSent) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(receiver.getAddress().getAddress(), port), connectionTimeout);
                socket.getOutputStream().write(gson.toJson(message).getBytes());
                socket.getOutputStream().flush();
                messageSent = true; // Message sent successfully
            } catch (SocketTimeoutException e) {
                System.out.println("Connection timeout to: " + receiver.getNodeId() + ". Retrying...");
                retries++;
            } catch (IOException e) {
                System.out.println("Failed to connect to: " + receiver.getNodeId() + ". Retrying...");
                retries++;
            }
        }

        if (!messageSent) {
            System.out.println("Unable to send message to: " + receiver.getNodeId());
        }
    }




    private void sendBlockchainMessage(BlockchainMessage message) {
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
