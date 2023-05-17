package com.myproject.game.network.kademlia;

import com.myproject.game.utils.Constants;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageDispatcher implements Runnable {
    private final RoutingTable routingTable;
    private final InMessageQueue inMessageQueue;
    private final OutMessageQueue outMessageQueue;
    private final ExecutorService messageWorkerService;


    public MessageDispatcher(RoutingTable routingTable, InMessageQueue inMessageQueue, OutMessageQueue outMessageQueue, int nWorkers) {
        this.routingTable = routingTable;
        this.inMessageQueue = inMessageQueue;
        this.outMessageQueue = outMessageQueue;
        messageWorkerService = Executors.newFixedThreadPool(nWorkers);
    }


    @Override
    public void run() {
        while (true) {
            // get message and submit it
            KademliaMessage message = inMessageQueue.getNextMessage();
            messageWorkerService.submit(new MessageTask(
                    routingTable,
                    outMessageQueue,
                    message
            ));
        }
    }
}

class MessageTask implements Runnable {
    private final RoutingTable routingTable;
    private final OutMessageQueue outMessageQueue;
    private final KademliaMessage message;

    public MessageTask(RoutingTable routingTable, OutMessageQueue outMessageQueue, KademliaMessage message) {
        this.routingTable = routingTable;
        this.outMessageQueue = outMessageQueue;
        this.message = message;

        System.out.println(message.toJson());
    }

    @Override
    public void run() {
        // Processing the message here!
        switch (message.getType()) {
            case FIND_NODE:
                processFindNode();
                break;
            case FIND_NODE_REPLY:
                System.out.println(Constants.GREEN + "Handling FIND_NODE_REPLY" + Constants.RESET);
                processFindNodeReply();
                break;
        }
    }

    private void processFindNode() {
        Node node = new Node(
                message.getSrcId(),
                new InetSocketAddress(message.getSrcAddress(), message.getSrcPort()),
                Objects.equals(routingTable.getBootstrapNode().getNodeId().toString(), message.getSrcId().getID())
        );
        System.out.println("helllo");
        System.out.println(node.toString());
        routingTable.insertNode(node);
        ArrayList<Node> closestNodesToTarget = routingTable.getClosestNodes(message.getSrcId(), 3);

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
        System.out.println(Constants.GREEN + "SENDING NODES" + Constants.RESET);
    }

    private void processFindNodeReply() {
        System.out.println("Processing nodes: " + message.toJson());

    }
}



