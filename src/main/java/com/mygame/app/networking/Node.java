package com.mygame.app.networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Node {

    private final String id;
    private final String ip;
    private final int port;
    private final boolean bootstrap;
    private long lastSeenTimestamp;

    public Node(String id, String ip, int port, boolean bootstrap, long lastSeenTimestamp) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.bootstrap = bootstrap;
        this.lastSeenTimestamp = lastSeenTimestamp;
    }


    public int getId() {
        return IDGenerator.hexStringToInt(id);
    }

    public String getIdHex() {
        return id;
    }

    public String getIp() {
        return ip;
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
        int y = IDGenerator.hexStringToInt(id);
        return x ^ y;
    }
}
