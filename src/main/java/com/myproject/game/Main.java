package com.myproject.game;

import com.google.common.eventbus.EventBus;
import com.myproject.game.network.blockchain.Blockchain;
import com.myproject.game.network.blockchain.BlockchainMessageType;
import com.myproject.game.network.kademlia.*;
import com.myproject.game.ui.GameGUI;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main {

    private static final EventBus eventBus = new EventBus();
    private static String localIp;
    private static String bootstrapIp = "172.18.0.2";
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
        Blockchain blockchain = new Blockchain(kademliaDHT, blockchainPort);



        System.out.println("This node ID: " + kademliaDHT.getNodeId() + " (hex)");
        System.out.println("Bootstrap ID: " + kademliaDHT.getBootstrapId() + " (hex)");

        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameGUI(eventBus);
            }
        });



        AtomicBoolean sent = new AtomicBoolean(false);
        Timer timer = new Timer(10000, e -> {
            if (!sent.get()) {
                blockchain.makeMatchmakingRequest(BlockchainMessageType.TTT_MATCHMAKING_REQUEST);
                System.out.println("should be in");
                sent.set(true);
            }
        });
        timer.start();
    }

}
