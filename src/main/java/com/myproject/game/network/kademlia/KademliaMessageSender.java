package com.myproject.game.network.kademlia;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;


public class KademliaMessageSender implements Runnable {
    private final OutMessageQueue outMessageQueue;
    private final DatagramSocket udpSocket;


    public KademliaMessageSender(OutMessageQueue outMessageQueue) {
        this.outMessageQueue = outMessageQueue;
        try {
            this.udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create DatagramSocket.", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            KademliaMessage message = outMessageQueue.getNextMessage();
            sendKademliaMessage(message);
        }
    }

    private void sendKademliaMessage(KademliaMessage message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        DatagramPacket packet;
        try {
            packet = new DatagramPacket(
                    jsonMessage.getBytes(),
                    jsonMessage.getBytes().length,
                    InetAddress.getByName(message.getDestAddress()),
                    5000
            );
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to create DatagramPacket.", e);
        }

        try {
            udpSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send DatagramPacket.", e);
        }

        System.out.println("Message sent: " + jsonMessage + "\n");
    }
}
