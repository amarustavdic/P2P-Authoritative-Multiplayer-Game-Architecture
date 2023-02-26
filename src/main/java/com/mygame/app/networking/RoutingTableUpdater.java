package com.mygame.app.networking;

import com.mygame.app.Constants;
import com.mygame.app.networking.messages.UDPMessage;
import com.mygame.app.networking.messages.UDPMessageBody;
import com.mygame.app.networking.messages.UDPMessageHeader;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

public class RoutingTableUpdater extends Thread {

    int refreshRate;

    public RoutingTableUpdater() {
        // for nodes not to have same com.mygame.app.networking.messages.RoutingTable refresh rate
        // to see easier what is going on
        Random rnd = new Random();
        int[] secs = {25,30,35,20,40,45};
        this.refreshRate = secs[rnd.nextInt(6)];
        System.out.println(
                        Constants.INFO + "com.mygame.app.networking.messages.RoutingTableUpdater refresh rate: "
                        + refreshRate + " sec" + Constants.RESET);
    }



    @Override
    public void run() {
        while (true) {

        }
    }
}
