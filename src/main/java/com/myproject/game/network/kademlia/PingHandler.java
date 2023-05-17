package com.myproject.game.network.kademlia;

import java.util.ArrayList;


public class PingHandler extends Thread {

    private RoutingTable routingTable;
    private int interval;
    private ArrayList<Node> pinged = new ArrayList<>();


    public PingHandler(RoutingTable routingTable, int interval) {
        this.interval = interval;
    }



    @Override
    public void run() {
        while (true) {

        }
    }


}
