package com.myproject.game.network.kademlia;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.time.Instant;

public class Node {
    private final boolean isBootstrap;
    private final KademliaID nodeId;
    private final InetSocketAddress address;
    private Instant lastSeen;

    public Node(KademliaID nodeId, InetSocketAddress address, boolean isBootstrap) {
        this.nodeId = nodeId;
        this.address = address;
        this.isBootstrap = isBootstrap;
        this.lastSeen = Instant.now();
    }

    public KademliaID getNodeId() {
        return nodeId;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public void updateLastSeen() {
        this.lastSeen = Instant.now();
    }

    public int getDistance(KademliaID target) {
        BigInteger distance = nodeId.getNumericID().xor(target.getNumericID());
        return distance.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node other = (Node) o;
        return nodeId.equals(other.nodeId);
    }

    @Override
    public int hashCode() {
        return nodeId.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", address=" + address +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
