package com.mygame.app.networking;


import java.util.ArrayList;

public class RoutingTableUpdater extends Thread {



    public RoutingTableUpdater() {


    }


    @Override
    public void run() {
        while (true) {
            /*
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */


            // generating FIND_NODES messages and adding them in queue
            String idHex = RoutingTable.getLocalNode().getIdHex();
            int k = 2;
            ArrayList<Node> kNodes = RoutingTable.getClosestNodes(IDGenerator.hexStringToInt(idHex),k);
            if (kNodes.size() < 2) kNodes.add(RoutingTable.getBootstrapNode());

            for (Node node : kNodes) {
                UDPMessage message = new UDPMessage(
                        node.getIdHex(),
                        UDPProtocol.FIND_NODE,
                        RoutingTable.getLocalNode().getIp(),
                        node.getIp());

                UDPMessageQueue.addMessage(message);
                System.out.println("Message added to queue: ");
                message.print();
            }


        }
    }
}
