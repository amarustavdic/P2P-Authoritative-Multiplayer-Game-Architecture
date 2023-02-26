package com.mygame.app.networking;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPReceiver extends Thread {

    private PingPongHandler pingPongHandler;
    private final DatagramSocket udpSocket;
    private byte[] inBuff;

    public UDPReceiver(PingPongHandler pingPongHandler) throws SocketException {
        this.pingPongHandler = pingPongHandler;
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


            // handling different message types that come in
            switch (message.getType()) {
                case FIND_NODE:
                    System.out.println("Find node message received!");
                    String receiver = message.getSender_ip();
                    Node n = message.getNode();
                    System.out.println("ID of reconstruated node: " + n.getIdHex());
                    RoutingTable.add(n);
                    UDPMessage reply = new UDPMessage(
                            UDPProtocol.NODE_FOUND,
                            RoutingTable.getLocalNode().getIdHex(),
                            RoutingTable.getBootstrapNode().getIp(),
                            receiver,
                            RoutingTable.getClosestNodes(IDGenerator.hexStringToInt(message.getSender_id()), 3)
                    );
                    UDPMessageQueue.addMessage(reply);
                    break;
                case NODE_FOUND:
                    System.out.println("Got discovered nodes!");
                    for (Node node : message.getNodes()) {
                        RoutingTable.add(node);
                    }
                    break;
                case PING:
                    System.out.println("Pinged by node with ID: " + message.getSender_id());
                    // send PONG message back!!
                    UDPMessage pong = new UDPMessage(
                            UDPProtocol.PONG,
                            RoutingTable.getLocalNode().getIdHex(),
                            RoutingTable.getLocalNode().getIp(),
                            message.getSender_ip()
                    );
                    UDPMessageQueue.addMessage(pong);
                    break;
                case PONG:
                    pingPongHandler.repliedWithPong(message.getSender_id());
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

