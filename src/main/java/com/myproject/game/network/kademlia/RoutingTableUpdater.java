package com.myproject.game.network.kademlia;


import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class RoutingTableUpdater extends Thread {

    private final RoutingTable routingTable;
    private final InMessageQueue inMessageQueue;
    private final OutMessageQueue outMessageQueue;
    private final int interval;
    private final long timeout;                        // Timeout duration in milliseconds

    public RoutingTableUpdater(RoutingTable routingTable, InMessageQueue inMessageQueue, OutMessageQueue outMessageQueue, int interval, int timeout) {
        this.routingTable = routingTable;
        this.inMessageQueue = inMessageQueue;
        this.outMessageQueue = outMessageQueue;
        this.interval = interval;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        while (true) {
            // Send FIND NODE messages... here...
            sendFindNodeMessages();


            try {
                // Wait for replies with a timeout
                waitForRepliesWithTimeout();

                // Process received replies
                processReplies();
            } catch (TimeoutException e) {
                // Handle timeout - no replies received within the timeout period
                handleTimeout();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

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
            if (routingTable.getLocalNode() != routingTable.getBootstrapNode()) {
                closestNodes.add(routingTable.getBootstrapNode()); // if no nodes, contact bootstrap
            } else {
                System.out.println("This is the BOOTSTRAP!");
            }
        }
        Node localNode = routingTable.getLocalNode();

        for (Node node : closestNodes) {
            KademliaMessage message = new KademliaMessage(
                    KademliaMessageType.FIND_NODE,
                    localNode.getAddress().getHostName(),
                    localNode.getAddress().getPort(),
                    node.getAddress().getHostName(),
                    node.getAddress().getPort(),
                    null
            );
            outMessageQueue.addMessage(message);
        }
    }

    private void waitForRepliesWithTimeout() throws TimeoutException, InterruptedException {
        long startTime = System.currentTimeMillis();

        while (true) {
            // Check if replies have been received
            if (repliesReceived()) {
                break;
            }

            // Check if timeout has elapsed
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= timeout) {
                throw new TimeoutException("Timeout - no replies received within the specified duration");
            }

            // Sleep for a short interval before checking again
            Thread.sleep(100); // Adjust the sleep duration as needed
        }
    }

    private boolean repliesReceived() {
        // Implement your logic to check if replies have been received
        // Return true if replies have been received, false otherwise
        return !inMessageQueue.isEmpty();
    }

    private void processReplies() {
        // Process received replies
        while (!inMessageQueue.isEmpty()) {
            KademliaMessage reply = inMessageQueue.getNextMessage();
            if (reply.getType() == KademliaMessageType.FIND_NODE_REPLY) {
                // Handle FIND_NODE_REPLY message
                // Add new nodes from the reply to your routing table

                System.out.println();
                //ArrayList<Node> nodes = reply.getNodes();
                //routingTable.addNodes(nodes);
            }
            // Handle other types of replies if needed
        }
    }

    private void handleTimeout() {
        // Handle timeout - no replies received within the timeout period
        System.out.println("Timeout - No replies received within the specified duration");

        // Perform any necessary actions when a timeout occurs
        // For example, you can re-send the FIND_NODE messages to the closest nodes or take other corrective measures.

        // Here's an example of re-sending FIND_NODE messages:
        sendFindNodeMessages();

        // You can also update any statistics or variables related to timeouts
        // For example, you can increment a counter to keep track of the number of timeouts that have occurred.
    }

}
