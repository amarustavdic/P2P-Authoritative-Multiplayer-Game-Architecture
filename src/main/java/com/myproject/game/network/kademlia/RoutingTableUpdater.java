package com.myproject.game.network.kademlia;

import java.util.ArrayList;
import java.util.Objects;

public class RoutingTableUpdater extends Thread {

    private RoutingTable routingTable;
    private int interval;

    public RoutingTableUpdater(RoutingTable routingTable, int interval) {
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
