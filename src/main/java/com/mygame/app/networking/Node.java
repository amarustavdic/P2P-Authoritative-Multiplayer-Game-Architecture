package com.mygame.app.networking;

import java.net.InetAddress;

public class Node {

    private final int id;
    private final InetAddress ip;
    private final int port;
    private final boolean bootstrap;
    private long lastSeenTimestamp;

    public Node(int id, InetAddress ip, int port, boolean bootstrap, long lastSeenTimestamp) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.bootstrap = bootstrap;
        this.lastSeenTimestamp = lastSeenTimestamp;
    }


    public int getId() {
        return id;
    }

    public InetAddress getIp() {
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
}
