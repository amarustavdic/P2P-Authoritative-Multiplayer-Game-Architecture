package com.myproject.game.network.kademlia;


import java.util.ArrayList;
import java.util.Objects;

public class RoutingTableUpdater extends Thread {

    private final RoutingTable routingTable;
    private final InMessageQueue inMessageQueue;
    private final OutMessageQueue outMessageQueue;
    private final int interval;


    public RoutingTableUpdater(RoutingTable routingTable, InMessageQueue inMessageQueue, OutMessageQueue outMessageQueue, int interval) {
        this.routingTable = routingTable;
        this.inMessageQueue = inMessageQueue;
        this.outMessageQueue = outMessageQueue;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {
            // Send FIND NODE messages... here...
            sendFindNodeMessages();

            try {
                sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendFindNodeMessages() {
        ArrayList<Node> closestNodes = routingTable.getClosestNodes(routingTable.getLocalNode().getNodeId(), routingTable.getAlpha());
        if (closestNodes.isEmpty()) {
            if (!Objects.equals(routingTable.getLocalNode().getNodeId().toString(), routingTable.getBootstrapNode().getNodeId().toString())) {
                closestNodes.add(routingTable.getBootstrapNode()); // if no nodes, contact bootstrap
            }
        }
        Node localNode = routingTable.getLocalNode();

        KademliaMessage message;
        for (Node node : closestNodes) {
            message = new KademliaMessage(
                    KademliaMessageType.FIND_NODE,
                    localNode.getNodeId(),
                    localNode.getAddress().getAddress().getHostAddress(),
                    localNode.getAddress().getPort(),
                    node.getNodeId(),
                    node.getAddress().getHostName(),
                    node.getAddress().getPort(),
                    " "
            );
            try {
                outMessageQueue.addMessage(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
