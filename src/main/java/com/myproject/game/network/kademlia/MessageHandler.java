package com.myproject.game.network.kademlia;

import com.myproject.game.utils.Constants;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Objects;


public class MessageHandler implements Runnable {
    private final RoutingTable routingTable;
    private final InMessageQueue inMessageQueue;
    private final OutMessageQueue outMessageQueue;


    public MessageHandler(RoutingTable routingTable, InMessageQueue inMessageQueue, OutMessageQueue outMessageQueue) {
        this.routingTable = routingTable;
        this.inMessageQueue = inMessageQueue;
        this.outMessageQueue = outMessageQueue;
    }


    @Override
    public void run() {
        while (true) {
            KademliaMessage message = inMessageQueue.getNextMessage();

            switch (message.getType()) {
                case FIND_NODE:
                    System.out.println(Constants.INFO + "IN QUEUE: " + inMessageQueue.length() + " messages" + Constants.RESET);
                    processFindNode(message);
                    break;
                case FIND_NODE_REPLY:
                    System.out.println(Constants.GREEN + "Handling FIND_NODE_REPLY" + Constants.RESET);
                    processFindNodeReply(message);
                    break;
            }
        }
    }



    private void processFindNode(KademliaMessage message) {
        Node node = new Node(
                message.getSrcId(),
                new InetSocketAddress(message.getSrcAddress(), message.getSrcPort()),
                Objects.equals(routingTable.getBootstrapNode().getNodeId().toString(), message.getSrcId().getID())
        );

        System.out.println(Constants.INFO + "NODE: " + node.toString() + Constants.RESET);
        System.out.println(Constants.INFO + "NEW NODE ADDED: " + routingTable.insertNode(node) + Constants.RESET);
        ArrayList<Node> closestNodesToTarget = routingTable.getClosestNodes(message.getSrcId(), 3);
        System.out.println(closestNodesToTarget.size());

        // send reply message with these nodes
        KademliaMessage reply = new KademliaMessage(
                KademliaMessageType.FIND_NODE_REPLY,
                routingTable.getLocalNode().getNodeId(),
                routingTable.getLocalNode().getAddress().toString(),
                routingTable.getLocalNode().getAddress().getPort(),
                message.getSrcId(),
                message.getSrcAddress(),
                message.getSrcPort(),
                closestNodesToTarget
        );
        outMessageQueue.addMessage(reply);
        System.out.println(Constants.GREEN + "REPLY SENT" + Constants.RESET);
    }

    private void processFindNodeReply(KademliaMessage message) {
        System.out.println("Processing nodes: " + message.toJson());

    }
}



