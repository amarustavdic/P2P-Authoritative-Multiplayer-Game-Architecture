package com.mygame.app.networking;

import java.util.*;

public class RoutingTable {

    // B / 2^K - used to calculate prefix length for a given bucket
    private static final int B = 8;  // ID size in number of bits
    private static final int K = 2;  // size of k-bucket (number of nodes it can store)
    private static List<List<Node>> buckets;
    private static Node localNode;
    private static Node bootstrapNode;


    public static void init(Node local, Node boot) {
        localNode = local;
        bootstrapNode = boot;
        buckets = new ArrayList<>((int)Math.pow(2, K));
        for (int i = 0; i < (int)Math.pow(2, K); i++) {
            buckets.add(new ArrayList<>(K));
        }
    }

    public static boolean add(Node node) {
        if (node.getId() == localNode.getId()) return false;
        int index = getIndex(localNode.getId(), node.getId());
        ArrayList<Node> bucket = (ArrayList<Node>) buckets.get(index);
        if (bucket.size() <= K) {
            bucket.remove(node);
            bucket.add(node);
        } else {
            bucket.remove(node);
            removeOldestNode(bucket);
            bucket.add(node);
        }
        return true;
    }

    public static boolean remove(Node node) {
        int index = getIndex(localNode.getId(), node.getId());
        ArrayList<Node> bucket = (ArrayList<Node>) buckets.get(index);
        bucket.remove(node);
        return true;
    }

    public static void print() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("BUCKET " + i);
            for (int j = 0; j < buckets.get(i).size(); j++) {
                int id = buckets.get(i).get(j).getId();
                System.out.print(id + "  ");
            }
            System.out.println();
        }
    }


    /*
    // k - number of the closest nodes retrieved
    public static ArrayList<Node> getClosestNodes(int id, int k) {

    }

    public static void updateLastSeen(Node node) {

    }

    public static int getBucketRange(int index) {

    }

    public void ping(Node node) {
        // update last seen time in routing table
        // or just remove node if it does  not respond to 2 pings in a row
    }

    public static Node findNode(String nodeId) {

    }

     */




    private static int getIndex(int localNodeId, int nodeId) {
        int distance = localNodeId ^ nodeId;
        int prefixLength = (int)(B / Math.pow(2, K));
        int offset = B - prefixLength;
        return distance >> offset;
    }

    public static void removeOldestNode(ArrayList<Node> nodes) {
        long oldestTimestamp = Long.MAX_VALUE;
        Node oldestNode = null;
        for (Node node : nodes) {
            if (node.getLastSeenTimestamp() < oldestTimestamp) {
                oldestTimestamp = node.getLastSeenTimestamp();
                oldestNode = node;
            }
        }
        if (oldestNode != null) {
            nodes.remove(oldestNode);
        }
    }


    public static Node getLocalNode() {
        return localNode;
    }

    public static Node getBootstrapNode() {
        return bootstrapNode;
    }
}
