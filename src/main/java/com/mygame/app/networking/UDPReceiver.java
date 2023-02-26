package com.mygame.app.networking;

import com.google.gson.Gson;
import com.mygame.app.Constants;
import com.mygame.app.networking.messages.UDPMessage;
import com.mygame.app.networking.messages.UDPMessageBody;
import com.mygame.app.networking.messages.UDPMessageHeader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class UDPReceiver extends Thread {

    private DatagramSocket udpSocket;
    private byte[] inBuff;
    private Gson gson;

    public UDPReceiver() throws SocketException {
        this.udpSocket = new DatagramSocket(RoutingTable.getBootstrapNode().getPort());
        this.gson = new Gson();
        this.inBuff = new byte[65535];
    }


    @Override
    public void run() {
        /*
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

            // for testing only
            System.out.print(Constants.STATUS + "Received message: " );
            message.print();
            System.out.print(Constants.RESET);


            switch (message.getProtocol()) {
                case DISCOVER_NODES:
                    boolean bootstrap = false;
                    if (RoutingTable.getBootstrapNode().getId() == message.getSenderID()) bootstrap = true;

                    Node senderNode = new Node(
                            message.getSenderID(),
                            message.getSenderIP(),
                            5000,
                            bootstrap,
                            0
                    );
                    RoutingTable.add(senderNode);

                    UDPMessageHeader header = new UDPMessageHeader(
                            UDPProtocol.DISCOVERED_NODES,
                            RoutingTable.getLocalNode().getIp(),
                            message.getSenderIP(),
                            RoutingTable.getLocalNode().getId()
                    );
                    UDPMessageBody body = new UDPMessageBody(closest);
                    UDPMessage msg = new UDPMessage(header, body);
                    UDPMessageQueue.addMessage(msg);
                    break;
                case DISCOVERED_NODES:
                    List<Node> received = message.getNodes();
                    if (received.size() > 0) {
                        for (Node node : received) {
                            RoutingTable.add(node);
                        }
                    }
                    break;
                default:

            }





            inBuff = new byte[65535];
        }


         */
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

