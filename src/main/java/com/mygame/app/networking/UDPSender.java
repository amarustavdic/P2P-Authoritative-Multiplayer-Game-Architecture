package com.mygame.app.networking;

import java.io.IOException;
import java.net.*;

public class UDPSender extends Thread {


    private final DatagramSocket udpSocket;

    public UDPSender() throws SocketException {
        this.udpSocket = new DatagramSocket();
    }


    @Override
    public void run() {
        while (true) {
            if (!UDPMessageQueue.isEmpty()) {
                UDPMessage message = UDPMessageQueue.getMessage();

                DatagramPacket packet = null;
                try {
                    packet = new DatagramPacket(
                            message.getBytes(),
                            message.getBytes().length,
                            InetAddress.getByName(message.getReceiver()),
                            5000
                    );
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                try {
                    udpSocket.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Message sent: ");
                message.print();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
