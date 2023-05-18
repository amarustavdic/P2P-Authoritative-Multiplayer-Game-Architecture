package com.myproject.game.network.kademlia;

import com.myproject.game.utils.Constants;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;


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
        KademliaMessage message = null;
        while (true) {
            message = inMessageQueue.getNextMessage();
            switch (message.getType()) {
                case FIND_NODE:
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
        // Reconstruct the source/sender node
        InetSocketAddress srcSocketAddress = new InetSocketAddress(message.getSrcAddress(), message.getSrcPort());
        boolean isBootstrapNode = routingTable.getBootstrapNode().getNodeId().equals(message.getSrcId().getID());
        Node senderNode = new Node(message.getSrcId(), srcSocketAddress, isBootstrapNode);

        // Insert the sender node into the routing table
        boolean isNewNode = routingTable.insertNode(senderNode);
        System.out.println(Constants.INFO + "New Node Added: " + isNewNode + Constants.RESET);

        // Get the closest nodes to the target from the routing table
        KademliaID targetId = message.getSrcId();
        int alpha = routingTable.getAlpha();
        ArrayList<Node> closestNodes = routingTable.getClosestNodes(targetId, alpha);

        System.out.println("checkpint 1");
        System.out.println(closestNodes.toString());

        // Send a reply message with the closest nodes
        KademliaMessage reply = new KademliaMessage(
                KademliaMessageType.FIND_NODE_REPLY,
                routingTable.getLocalNode().getNodeId(),
                routingTable.getLocalNode().getAddress().getAddress().getHostAddress(),
                routingTable.getLocalNode().getAddress().getPort(),
                message.getSrcId(),
                message.getSrcAddress(),
                message.getSrcPort(),
                closestNodes
        );
        System.out.println("checkpint 2");

        try {
            System.out.println("checkpint 3");
            outMessageQueue.addMessage(reply);
        } catch (InterruptedException e) {
            System.out.println("checkpint 4");
            e.printStackTrace();
        }

        System.out.println(Constants.GREEN + "Reply Sent" + Constants.RESET);
    }


    private void processFindNodeReply(KademliaMessage message) {
        System.out.println("Processing nodes: " + message.toJson());

    }
}



