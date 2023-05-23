package com.myproject.game.network.kademlia;

import com.google.gson.*;
import com.myproject.game.utils.Constants;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;



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
                    processFindNodeReply(message);
                    break;
            }
        }
    }



    private void processFindNode(KademliaMessage message) {
        // Reconstruct the source/sender node
        InetSocketAddress srcSocketAddress = new InetSocketAddress(message.getSrcAddress(), message.getSrcPort());
        boolean isBootstrapNode = routingTable.getBootstrapNode().getNodeId().getID().equals(message.getSrcId().getID());
        Node senderNode = new Node(message.getSrcId(), srcSocketAddress, isBootstrapNode);

        // Insert the sender node into the routing table
        boolean isNewNode = routingTable.insertNode(senderNode);
        //System.out.println(Constants.INFO + "New Node Added: " + isNewNode + Constants.RESET);

        // Get the closest nodes to the target from the routing table
        KademliaID targetId = message.getSrcId();
        int alpha = routingTable.getAlpha();
        ArrayList<Node> closestNodes = routingTable.getClosestNodes(targetId, alpha);


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

        try {
            // adding reply to the out queue
            outMessageQueue.addMessage(reply);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void processFindNodeReply(KademliaMessage message) {
        Gson gson = new Gson();
        JsonArray foundNodes = gson.fromJson(message.getPayload(), JsonArray.class);
        for (JsonElement nodeData : foundNodes) {
            JsonObject object = nodeData.getAsJsonObject();

            // Extracting all the necessary data here:
            KademliaID kademliaID = new KademliaID(object.get("nodeId").getAsJsonObject().get("id").getAsString());

            String ipAddress = object.getAsJsonObject("address").get("address").getAsString();
            int port = object.getAsJsonObject("address").get("port").getAsInt();
            InetSocketAddress address = new InetSocketAddress(ipAddress, port);

            boolean isBootstrap = object.get("isBootstrap").getAsBoolean();

            Instant lastSeen = Instant.ofEpochSecond(
                    object.get("lastSeen").getAsJsonObject().get("seconds").getAsLong(),
                    object.get("lastSeen").getAsJsonObject().get("nanos").getAsLong()
            );

            // creating node object from data
            Node node = new Node(kademliaID, address, isBootstrap, lastSeen);

            boolean inserted = routingTable.insertNode(node);
            // adding the node in my routing table
            //System.out.println(Constants.INFO + "New Node Added: " + inserted + Constants.RESET);
        }
    }

}



