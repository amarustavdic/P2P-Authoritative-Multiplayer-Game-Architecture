package com.myproject.game.network.kademlia;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.*;

public class RoutingTable {

    // alpha determines the maximum number of nodes that can be contacted in parallel
    // when performing iterative routing or lookups in the distributed hash table.
    // B / 2^K - used to calculate prefix length for a given bucket
    private int B;       // ID size in number of bits
    private int K;       // size of k-bucket (number of nodes it can store)
    private int alpha;
    private List<List<KademliaNode>> buckets;
    private KademliaNode localKademliaNode;
    private KademliaNode bootstrapKademliaNode;


    public RoutingTable(InetAddress ip, int port, boolean isBootstrapNode, int B, int K) {
        IDGenerator.generateID(ip.getHostAddress()+port);
        KademliaNode node = new KademliaNode(
                IDGenerator.getIdAsString(),
                ip.getHostAddress(),
                port,
                isBootstrapNode,
                0L
        );
        if (isBootstrapNode) {
            localKademliaNode = bootstrapKademliaNode = node;
        } else {
            localKademliaNode = node;
            IDGenerator.generateID("172.17.0.2"+port);
            bootstrapKademliaNode = new KademliaNode(
                    IDGenerator.getIdAsString(),
                    "172.17.0.2",
                    port,
                    true,
                    0L
            );
        }

        buckets = new ArrayList<>((int)Math.pow(2, K));
        for (int i = 0; i < (int)Math.pow(2, K); i++) {
            buckets.add(new ArrayList<>(K));
        }
    }



    public boolean add(KademliaNode kademliaNode) {
        if (kademliaNode.getNodeId() == localKademliaNode.getNodeId()) return false;
        int index = getIndex(localKademliaNode.getNodeId(), kademliaNode.getNodeId());
        ArrayList<KademliaNode> bucket = (ArrayList<KademliaNode>) buckets.get(index);
        if (bucket.size() < K) {
            remove(kademliaNode);
            bucket.add(kademliaNode);
        } else {
            if (remove(kademliaNode)) bucket.add(kademliaNode);
            else {
                removeOldestNode(bucket);
                bucket.add(kademliaNode);
            }
        }
        return true;
    }

    public boolean remove(KademliaNode kademliaNode) {
        int index = getIndex(localKademliaNode.getNodeId(), kademliaNode.getNodeId());
        ArrayList<KademliaNode> bucket = (ArrayList<KademliaNode>) buckets.get(index);
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).getNodeId() == kademliaNode.getNodeId()) {
                bucket.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean remove(String nodeIdHex) {
        int index = getIndex(localKademliaNode.getNodeId(), IDGenerator.hexStringToInt(nodeIdHex));
        ArrayList<KademliaNode> bucket = (ArrayList<KademliaNode>) buckets.get(index);
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).getNodeId() == IDGenerator.hexStringToInt(nodeIdHex)) {
                bucket.remove(i);
                return true;
            }
        }
        return false;
    }

    // k - number of the closest nodes retrieved
    public ArrayList<KademliaNode> getClosestNodes(int id, int k) {
        ArrayList<KademliaNode> closestKademliaNodes = new ArrayList<KademliaNode>();
        PriorityQueue<KademliaNode> pq = new PriorityQueue<KademliaNode>(
                Comparator.comparingInt(n -> n.getDistance(IDGenerator.intToHexString(id))));
        for (List<KademliaNode> bucket : buckets) {
            for (KademliaNode kademliaNode : bucket) {
                if (kademliaNode.getNodeId() != localKademliaNode.getNodeId()) {
                    pq.offer(kademliaNode);
                }
            }
        }
        while (!pq.isEmpty() && closestKademliaNodes.size() < k) {
            closestKademliaNodes.add(pq.poll());
        }
        return closestKademliaNodes;
    }

    // I will see later what am I going to do about this one
    // when I will actually need to use it
    public void updateLastSeen(String nodeIdHex) {
        int index = getIndex(localKademliaNode.getNodeId(), IDGenerator.hexStringToInt(nodeIdHex));
        List<KademliaNode> bucket = buckets.get(index);
        for (KademliaNode n : bucket) {
            if (n.getNodeId() == IDGenerator.hexStringToInt(nodeIdHex)) {
                n.setLastSeenTimestamp(Instant.now().getEpochSecond());
                return;
            }
        }
    }




    private int getIndex(int localNodeId, int nodeId) {
        int distance = localNodeId ^ nodeId;
        int prefixLength = (int)(B / Math.pow(2, K));
        int offset = B - prefixLength;
        return distance >> offset;
    }

    public static void removeOldestNode(ArrayList<KademliaNode> kademliaNodes) {
        long oldestTimestamp = Long.MAX_VALUE;
        KademliaNode oldestKademliaNode = null;
        for (KademliaNode kademliaNode : kademliaNodes) {
            if (kademliaNode.getLastSeenTimestamp() < oldestTimestamp) {
                oldestTimestamp = kademliaNode.getLastSeenTimestamp();
                oldestKademliaNode = kademliaNode;
            }
        }
        if (oldestKademliaNode != null) {
            kademliaNodes.remove(oldestKademliaNode);
        }
    }


    public KademliaNode getLocalNode() {
        return localKademliaNode;
    }

    public KademliaNode getBootstrapNode() {
        return bootstrapKademliaNode;
    }

    public ArrayList<KademliaNode> getAllNodes() {
        ArrayList<KademliaNode> allKademliaNodes = new ArrayList<>();
        for (List<KademliaNode> bucket : buckets) {
            allKademliaNodes.addAll(bucket);
        }
        return allKademliaNodes;
    }







    // for testing purposes only
    public void print() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("BUCKET " + i);
            for (int j = 0; j < buckets.get(i).size(); j++) {
                int id = buckets.get(i).get(j).getNodeId();
                System.out.print(IDGenerator.intToHexString(id) + "  ");
            }
            System.out.println();
        }
    }
}
