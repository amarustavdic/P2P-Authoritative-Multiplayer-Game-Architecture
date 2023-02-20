package com.mygame.app.networking;

import com.mygame.app.Constants;
import com.mygame.app.networking.messages.UDPMessage;
import com.mygame.app.networking.messages.UDPMessageBody;
import com.mygame.app.networking.messages.UDPMessageHeader;

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
            try {
                sleep(refreshRate*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Node localNode = RoutingTable.getLocalNode();
            // for testing purposes getting only one closest node
            List<Node> closest = RoutingTable.getClosestNodes(localNode,1);

            // generating messages to add them to com.mygame.app.networking.messages.UDPMessageQueue
            if (closest.size() > 0) {
                for (Node node : closest) {
                    UDPMessageHeader header = new UDPMessageHeader(
                            UDPProtocol.DISCOVER_NODES,
                            localNode.getIp(),
                            node.getIp(),
                            localNode.getId()
                    );
                    UDPMessageBody body = new UDPMessageBody(null);
                    UDPMessage message = new UDPMessage(header, body);
                    UDPMessageQueue.addMessage(message);
                }
            } else {
                if (!RoutingTable.getLocalNode().isBootstrap()) {
                    UDPMessageHeader header = new UDPMessageHeader(
                            UDPProtocol.DISCOVER_NODES,
                            localNode.getIp(),
                            RoutingTable.getBootstrapNode().getIp(),
                            localNode.getId()
                    );
                    UDPMessageBody body = new UDPMessageBody(null);
                    UDPMessage message = new UDPMessage(header, body);
                    UDPMessageQueue.addMessage(message);
                }
            }
        }
    }
}
