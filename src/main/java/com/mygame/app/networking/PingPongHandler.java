package com.mygame.app.networking;

import com.mygame.app.Constants;

import java.util.ArrayList;


public class PingPongHandler extends Thread {

    private int interval;

    public PingPongHandler(int interval) {
        this.interval = interval;
    }



    @Override
    public void run() {
        while (true) {
            try {
                // this is how much time nodes have to reply to PING message and also interval of ping ponger thread
                sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // here add function to remove all nodes that did not reply with PONG message
            // to make it more reliable, could change it to this:
            //      Node get only deleted if it does not reply to two consecutive PING messages
            //      in case if firs UDP message don't make it to the end node
            if (PingedList.pinged.size() != 0) {
                ArrayList<Node> noReplyNodes = PingedList.pinged;
                for (Node node : noReplyNodes) {
                    boolean removed = RoutingTable.remove(node.getIdHex());
                    if (removed) {
                        System.out.println(Constants.RED + "Node with ID: " + node.getIdHex() + " REMOVED (ping not answered)" + Constants.RESET);
                    }
                }
            }

            // pinging all nodes I know
            ArrayList<Node> nodes = RoutingTable.getAllNodes();
            for (Node node : nodes) {
                ping(node);
            }
        }
    }


    // generates ping message and adds it to message queue
    private void ping(Node node) {
        String senderId, senderIp, receiverIp;
        senderId = RoutingTable.getLocalNode().getIdHex();
        senderIp = RoutingTable.getLocalNode().getIp();
        receiverIp = node.getIp();
        UDPMessage message = new UDPMessage(UDPProtocol.PING, senderId, senderIp, receiverIp);
        UDPMessageQueue.addMessage(message);
        PingedList.pinged.add(node);
    }

    public void repliedWithPong(String nodeId) {
        PingedList.removeNode(nodeId);
        RoutingTable.updateLastSeen(nodeId);
    }
}
