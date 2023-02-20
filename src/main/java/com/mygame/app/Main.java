package com.mygame.app;

import com.mygame.app.Constants;
import com.mygame.app.networking.RoutingTable;
import com.mygame.app.networking.RoutingTableUpdater;
import com.mygame.app.networking.UDPReceiver;
import com.mygame.app.networking.UDPSender;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException {
        // for testing purposes only
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println(Constants.INFO + "IP address of the machine: " + ip.getHostAddress() + Constants.RESET);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }




        RoutingTable.init();
        ExecutorService executorService = Executors.newCachedThreadPool();
        UDPReceiver udpReceiver = new UDPReceiver();
        UDPSender udpSender = new UDPSender();
        RoutingTableUpdater RTUpdater = new RoutingTableUpdater();

        executorService.submit(udpReceiver);
        executorService.submit(udpSender);
        executorService.submit(RTUpdater);




        // trying to figure out matchmaking
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (str.equals("match")) {
                //List<com.mygame.app.networking.messages.Node> candidates = findMatchCandidate();
            }
        }

    }

    /*
    public static List<com.mygame.app.networking.messages.Node> findMatchCandidate() {
        // get random ID in keyspace
        // get k-closest Nodes
        // store them in your list of candidates
        // and query them too
        // using this RPC call every reply is going to be sent to first initiator of call
        // limit the list of candidates to some resonable number,
        // depending on how big the network is
    }

     */

}
