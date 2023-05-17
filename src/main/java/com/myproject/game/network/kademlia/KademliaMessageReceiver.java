package com.myproject.game.network.kademlia;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KademliaMessageReceiver implements Runnable {
    private final InMessageQueue inMessageQueue;
    private final DatagramSocket udpSocket;
    private final byte[] inBuff;

    public KademliaMessageReceiver(InMessageQueue inMessageQueue) {
        this.inMessageQueue = inMessageQueue;
        try {
            this.udpSocket = new DatagramSocket(5000);
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create DatagramSocket.", e);
        }
        this.inBuff = new byte[65535];
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket dpReceive = new DatagramPacket(inBuff, inBuff.length);
            try {
                udpSocket.receive(dpReceive);
            } catch (IOException e) {
                throw new RuntimeException("Failed to receive DatagramPacket.", e);
            }
        }
    }

}
