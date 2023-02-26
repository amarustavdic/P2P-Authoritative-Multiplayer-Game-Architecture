package com.mygame.app;

import com.mygame.app.networking.*;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private static String localIp;
    private static String bootstrapIp = "172.17.0.2";
    private static final int port = 5000;
    private static final int B = 8;
    private static final int K = 2;

    public static void main(String[] args) throws IOException {
        setLocalIp();
        initRoutingTable(args);

        // only for testing
        Node local = RoutingTable.getLocalNode();
        System.out.println(Constants.INFO+"IP: "+local.getIp()+"    ID: "+local.getIdHex()+Constants.RESET);


        ExecutorService executorService = Executors.newCachedThreadPool();
        UDPSender udpSender = new UDPSender();
        RoutingTableUpdater RTUpdater = new RoutingTableUpdater();

        executorService.submit(udpSender);
        executorService.submit(RTUpdater);




        // trying to figure out matchmaking
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (str.equals("/discover")) {

            }
        }

    }


    public static void initRoutingTable(String[] args) {
        boolean isBootstrap;
        if (args.length == 0) isBootstrap = false;
        else isBootstrap = Boolean.parseBoolean(args[0]);
        // generating ID of node using SHA-1
        IDGenerator.init(B);
        IDGenerator.generateID(bootstrapIp+port);
        Node bootNode = new Node(IDGenerator.getIdAsString(), bootstrapIp, port, true, 0);
        IDGenerator.generateID(localIp+port);
        Node localNode = new Node(IDGenerator.getIdAsString(), localIp, port, false, 0);
        if (isBootstrap) localNode = bootNode;
        RoutingTable.init(localNode, bootNode, B, K);
    }

    private static void setLocalIp() {
        localIp = null;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
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
