package com.myproject.game.network.blockchain;

import com.google.gson.Gson;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.kademlia.Node;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class BlockchainMessageSender implements Runnable {
    private final KademliaDHT dht;
    private final BlockchainOutbox outbox;
    private final Socket socket;
    private final int port;


    public BlockchainMessageSender(KademliaDHT dht, BlockchainOutbox outbox, int port) {
        this.dht = dht;
        this.outbox = outbox;
        this.port = port;
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
        while (!peers.isEmpty()) {
            Node peer = peers.get(0);
            peers.remove(0);
            try {
                socket.connect(new InetSocketAddress(peer.getAddress().getAddress(), port));
            } catch (IOException e) {

                System.out.println("Unable to connect to: " + peer.getNodeId());
                throw new RuntimeException("Failed to connect to the receiver's IP address.", e);
            }

            try {
                socket.getOutputStream().write(jsonMessage.getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to send the message.", e);
            }
        }
    }
}
