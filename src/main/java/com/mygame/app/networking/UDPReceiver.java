package com.mygame.app.networking;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPReceiver extends Thread {

    private final DatagramSocket udpSocket;
    private byte[] inBuff;

    public UDPReceiver() throws SocketException {
        this.udpSocket = new DatagramSocket(5000);
        this.inBuff = new byte[65535];
    }


    @Override
    public void run() {
        DatagramPacket DpReceive = null;
        while (true) {
            DpReceive = new DatagramPacket(inBuff, inBuff.length);
            try {
                udpSocket.receive(DpReceive);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String jsonStr = String.valueOf(data(inBuff));
            UDPMessage message = new Gson().fromJson(jsonStr, UDPMessage.class);



            switch (message.getType()) {
                case FIND_NODE:
                    System.out.println("Find node message received!");
                    String receiver = message.getSender();
                    Node n = message.getNode();
                    System.out.println("ID of reconstruated node: " + n.getIdHex());
                    RoutingTable.add(n);
                    UDPMessage reply = new UDPMessage(
                            RoutingTable.getLocalNode().getIdHex(),
                            UDPProtocol.DISCOVERED_NODES,
                            RoutingTable.getBootstrapNode().getIp(),
                            receiver,
                            RoutingTable.getClosestNodes(IDGenerator.hexStringToInt(message.getId()), 3)
                    );
                    UDPMessageQueue.addMessage(reply);
                    break;
                case DISCOVERED_NODES:
                    System.out.println("Got discovered nodes!");
                    for (Node node : message.getNodes()) {
                        RoutingTable.add(node);
                    }

                    break;
                default:

                    break;
            }
            inBuff = new byte[65535];
        }
    }



    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}

