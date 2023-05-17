package com.myproject.game.network.kademlia;

import java.util.ArrayList;
import java.util.Objects;

public class RoutingTableUpdater extends Thread {

    private RoutingTable routingTable;
    private int interval;

    public RoutingTableUpdater(RoutingTable routingTable, int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {
            // generating FIND_NODES messages and adding them in queue
            String idHex = routingTable.getLocalNode().getIdHex();
            int k = 2;  // k-number of the closest nodes that get queried
            ArrayList<KademliaNode> kKademliaNodes = routingTable.getClosestNodes(IDGenerator.hexStringToInt(idHex),k);
            if (kKademliaNodes.size() < 2) kKademliaNodes.add(routingTable.getBootstrapNode());

            for (KademliaNode kademliaNode : kKademliaNodes) {
                String localIp = routingTable.getLocalNode().getNodeIp();
                if (!Objects.equals(localIp, kademliaNode.getNodeIp())) {
                    KademliaMessage message = new KademliaMessage(
                            KademliaRpcType.FIND_NODE,                     // message protocol
                            routingTable.getLocalNode().getIdHex(),    // sender ID in HEX string
                            localIp,
                            kademliaNode.getNodeIp());

                    //UDPMessageQueue.addMessage(message);
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
