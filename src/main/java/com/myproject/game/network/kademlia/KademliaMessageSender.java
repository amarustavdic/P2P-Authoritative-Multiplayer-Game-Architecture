package com.myproject.game.network.kademlia;

import java.io.IOException;
import java.net.*;

public class KademliaMessageSender implements Runnable {
    private final OutMessageQueue outMessageQueue;
    private DatagramSocket udpSocket;
    private boolean running;

    public KademliaMessageSender(OutMessageQueue outMessageQueue) {
        this.outMessageQueue = outMessageQueue;
        try {
            this.udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create DatagramSocket.", e);
        }
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (outMessageQueue) {
                while (outMessageQueue.isEmpty()) {
                    try {
                        outMessageQueue.wait(); // Wait until the queue is not empty
                    } catch (InterruptedException e) {
                        throw new RuntimeException("KademliaMessageSender thread was interrupted.", e);
                    }
                }
            }

            if (outMessageQueue.isEmpty()) {
                continue; // Check again if the queue is empty after waking up
            }

            KademliaMessage message = outMessageQueue.getNextMessage();
            sendKademliaMessage(message);
        }

        // Close the DatagramSocket when the thread stops
        udpSocket.close();
    }

    private void sendKademliaMessage(KademliaMessage message) {
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(
                    message.getBytes(),
                    message.getBytes().length,
                    InetAddress.getByName(message.getReceiver_ip()),
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

        System.out.print("Message sent: ");
        message.print();
    }

    public void stopSender() {
        running = false;
        synchronized (outMessageQueue) {
            outMessageQueue.notify(); // Wake up the thread in case it's waiting
        }
    }
}
