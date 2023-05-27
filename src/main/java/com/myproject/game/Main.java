package com.myproject.game;

import com.myproject.game.network.blockchain.Blockchain;
import com.myproject.game.network.kademlia.*;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Main {

    private static String localIp;
    private static String bootstrapIp = "172.17.0.2";
    private static boolean isBootstrap;
    private static final int port = 5000;
    private static final int B = 8;    // number of bits for ID space
    private static final int K = 2;    // max nodes per bucket
    private static final int alpha = 2; // max  number of nodes being contacted in parallel

    private static final int blockchainPort = 7000;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) isBootstrap = false;
        else isBootstrap = Boolean.parseBoolean(args[0]);
        localIp = InetAddress.getLocalHost().getHostAddress();

        KademliaDHT kademliaDHT = new KademliaDHT(InetAddress.getByName(localIp), port, isBootstrap, B, K, alpha);
        Blockchain blockchain = new Blockchain(kademliaDHT, blockchainPort, isBootstrap);


        System.out.println("This node ID: " + kademliaDHT.getNodeId() + " (hex)");
        System.out.println("Bootstrap ID: " + kademliaDHT.getBootstrapId() + " (hex)");

        // run GUI
        // SwingUtilities.invokeLater(GameGUI::new);







        // just testing the shufling functionality of the arraylist
        // Create an ArrayList
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);

        // Set the random seed
        long seed = 123; // Replace with your desired seed value
        Collections.shuffle(numbers, new Random(seed));

        // Print the shuffled ArrayList
        System.out.println("Shuffled ArrayList: " + numbers);









    }

}
