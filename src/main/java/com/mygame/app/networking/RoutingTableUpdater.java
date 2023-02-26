package com.mygame.app.networking;

import java.util.ArrayList;

public class RoutingTableUpdater extends Thread {


    @Override
    public void run() {
        while (true) {
            // generating FIND_NODES messages and adding them in queue
            String idHex = RoutingTable.getLocalNode().getIdHex();
            int k = 2;  // k-number of the closest nodes that get queried
            ArrayList<Node> kNodes = RoutingTable.getClosestNodes(IDGenerator.hexStringToInt(idHex),k);
            if (kNodes.size() < 2) kNodes.add(RoutingTable.getBootstrapNode());

            for (Node node : kNodes) {
                String localIp = RoutingTable.getLocalNode().getIp();
                if (localIp != node.getIp()) {
                    UDPMessage message = new UDPMessage(
                            RoutingTable.getLocalNode().getIdHex(),    // sender ID in HEX string
                            UDPProtocol.FIND_NODE,                     // message protocol
                            localIp,
                            node.getIp());

                    UDPMessageQueue.addMessage(message);
                    System.out.println("Message added to queue: ");
                    message.print();
                }
            }


            try {
                sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
