package com.mygame.app.networking;

import java.util.ArrayList;
import java.util.Objects;

public class RoutingTableUpdater extends Thread {

    private int interval;

    public RoutingTableUpdater(int interval) {
        this.interval = interval;
    }

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
                if (!Objects.equals(localIp, node.getIp())) {
                    UDPMessage message = new UDPMessage(
                            UDPProtocol.FIND_NODE,                     // message protocol
                            RoutingTable.getLocalNode().getIdHex(),    // sender ID in HEX string
                            localIp,
                            node.getIp());

                    UDPMessageQueue.addMessage(message);
                    System.out.println("Message added to queue: ");
                    message.print();
                }
            }


            try {
                sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
