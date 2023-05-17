package com.myproject.game.network.kademlia;

import com.myproject.game.utils.Constants;

import java.util.ArrayList;


public class PingHandler extends Thread {

    private RoutingTable routingTable;
    private int interval;
    private ArrayList<KademliaNode> pinged = new ArrayList<>();


    public PingHandler(RoutingTable routingTable, int interval) {
        this.interval = interval;
    }



    @Override
    public void run() {
        while (true) {

        }
    }


}
