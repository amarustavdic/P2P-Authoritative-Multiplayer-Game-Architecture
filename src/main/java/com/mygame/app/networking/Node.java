package com.mygame.app.networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Node {

    private final int id;
    private final String ip;
    private final int port;
    private final boolean bootstrap;
    private long lastSeenTimestamp;

    public Node(int id, String ip, int port, boolean bootstrap, long lastSeenTimestamp) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.bootstrap = bootstrap;
        this.lastSeenTimestamp = lastSeenTimestamp;
    }


    public int getId() {
        return id;
    }

    public InetAddress getIp() throws UnknownHostException {
        return InetAddress.getByName(ip);
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
