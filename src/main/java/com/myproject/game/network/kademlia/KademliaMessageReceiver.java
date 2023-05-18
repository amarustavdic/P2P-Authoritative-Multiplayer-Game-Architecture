package com.myproject.game.network.kademlia;

import com.google.gson.Gson;
import com.myproject.game.utils.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class KademliaMessageReceiver implements Runnable {
    private final InMessageQueue inMessageQueue;
    private final DatagramSocket udpSocket;
    private final byte[] inBuff;
    private final Gson gson;

    public KademliaMessageReceiver(InMessageQueue inMessageQueue) {
        this.inMessageQueue = inMessageQueue;
        try {
            this.udpSocket = new DatagramSocket(5000);
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create DatagramSocket.", e);
        }
        this.inBuff = new byte[65535];
        this.gson = new Gson();
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket dpReceive = new DatagramPacket(inBuff, inBuff.length);
            try {
                udpSocket.receive(dpReceive);
                String jsonMessage = new String(dpReceive.getData(), 0, dpReceive.getLength());
                KademliaMessage receivedMessage = gson.fromJson(jsonMessage, KademliaMessage.class);
                inMessageQueue.addMessage(receivedMessage);

                System.out.println(Constants.INFO + "Received message: " + receivedMessage.toJson() + "\n" + Constants.RESET);
            } catch (IOException e) {
                throw new RuntimeException("Failed to receive DatagramPacket.", e);
            }
        }
    }
}
