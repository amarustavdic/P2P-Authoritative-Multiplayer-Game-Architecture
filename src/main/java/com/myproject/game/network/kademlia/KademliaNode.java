package com.myproject.game.network.kademlia;

public class KademliaNode {

    private final String nodeId;
    private final String nodeIp;
    private final int port;
    private final boolean bootstrap;
    private long lastSeenTimestamp;

    public KademliaNode(String nodeId, String nodeIp, int port, boolean bootstrap, long lastSeenTimestamp) {
        this.nodeId = nodeId;
        this.nodeIp = nodeIp;
        this.port = port;
        this.bootstrap = bootstrap;
        this.lastSeenTimestamp = lastSeenTimestamp;
    }


    public int getNodeId() {
        return IDGenerator.hexStringToInt(nodeId);
    }

    public String getIdHex() {
        return nodeId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public int getPort() {
        return port;
    }

    public boolean isBootstrap() {
        return bootstrap;
    }

    public long getLastSeenTimestamp() {
        return lastSeenTimestamp;
    }

    public void setLastSeenTimestamp(long lastSeenTimestamp) {
        this.lastSeenTimestamp = lastSeenTimestamp;
    }

    public int getDistance(String nodeId) {
        int x = IDGenerator.hexStringToInt(nodeId);
        int y = IDGenerator.hexStringToInt(this.nodeId);
        return x ^ y;
    }
}
