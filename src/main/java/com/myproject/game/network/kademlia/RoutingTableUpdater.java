package com.myproject.game.network.kademlia;

import java.util.ArrayList;
import java.util.Objects;

public class RoutingTableUpdater extends Thread {

    private final RoutingTable routingTable;
    private final OutMessageQueue outMessageQueue;
    private final int interval;

    public RoutingTableUpdater(RoutingTable routingTable, OutMessageQueue outMessageQueue, int interval) {
        this.routingTable = routingTable;
        this.outMessageQueue = outMessageQueue;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {




            try {
                sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
