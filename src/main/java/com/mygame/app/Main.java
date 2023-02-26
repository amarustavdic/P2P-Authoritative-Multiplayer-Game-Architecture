package com.mygame.app;

import com.mygame.app.networking.*;

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



        Node local = new Node(135, "172.168.0.14", 8000, false, 2394857);
        Node boot = new Node(221, "192.168.1.1", 8000, true, 985631);
        Node node2 = new Node(254, "192.168.1.2", 8000, false, 876543);
        Node node3 = new Node(253, "192.168.1.3", 8000, false, 745123);
        Node node4 = new Node(252, "192.168.1.4", 8000, false, 923456);
        Node node5 = new Node(78, "192.168.1.5", 8000, false, 456789);
        Node node6 = new Node(12, "192.168.1.6", 8000, false, 234567);
        Node node7 = new Node(56, "192.168.1.7", 8000, false, 567891);
        Node node8 = new Node(90, "192.168.1.8", 8000, false, 345678);
        Node node9 = new Node(21, "192.168.1.9", 8000, false, 678912);
        Node node10 = new Node(165, "192.168.1.10", 8000, false, 789123);

        RoutingTable.init(local, boot);
        System.out.println("Added node: " + RoutingTable.add(node2) + "  ID: " + node2.getId());
        System.out.println("Added node: " + RoutingTable.add(local) + "  ID: " + local.getId());
        System.out.println("Added node: " + RoutingTable.add(node3) + "  ID: " + node3.getId());
        System.out.println("Added node: " + RoutingTable.add(node4) + "  ID: " + node4.getId());

        System.out.println("Removed node: " + RoutingTable.remove(node4) + "  ID: " + node4.getId());
        System.out.println("Added node: " + RoutingTable.add(node4) + "  ID: " + node4.getId());
        System.out.println("Added node: " + RoutingTable.add(node4) + "  ID: " + node4.getId());

        RoutingTable.print();





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
