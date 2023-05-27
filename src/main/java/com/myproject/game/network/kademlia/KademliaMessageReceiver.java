package com.myproject.game.network.kademlia;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class KademliaMessageReceiver implements Runnable {
    private static final int BUFFER_SIZE = 2048; // Adjust buffer size as needed

    private final InMessageQueue inMessageQueue;
    private final Gson gson;
    private final DatagramSocket udpSocket;
    private final byte[] buffer;

    public KademliaMessageReceiver(InMessageQueue inMessageQueue, int port) throws SocketException {
        this.inMessageQueue = inMessageQueue;
        this.gson = new Gson();
        this.buffer = new byte[BUFFER_SIZE];
        this.udpSocket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
            try {
                udpSocket.receive(packet);
                processReceivedPacket(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processReceivedPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        int length = packet.getLength();

        KademliaMessage receivedMessage = gson.fromJson(new String(data, 0, length), KademliaMessage.class);
        inMessageQueue.addMessage(receivedMessage);

        // Clear the buffer
        packet.setLength(BUFFER_SIZE);
    }
}
