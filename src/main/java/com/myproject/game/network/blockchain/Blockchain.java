package com.myproject.game.network.blockchain;

import com.myproject.game.network.kademlia.KademliaDHT;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Blockchain {

    private int port;
    private KademliaDHT dht;
    private BlockchainOutbox outbox;
    private BlockchainInbox inbox;
    private ArrayList<Block> chain;
    private BlockchainMessageSender sender;
    private BlockchainMessageReceiver receiver;


    public Blockchain(KademliaDHT dht, int port) {
        this.port = port;
        this.dht = dht;
        this.chain = new ArrayList<>();
        this.inbox = new BlockchainInbox();
        this.outbox = new BlockchainOutbox();
        this.sender = new BlockchainMessageSender(dht, outbox, port,1000, 2);
        this.receiver = new BlockchainMessageReceiver(port);



        // genesis block hardcoded
        this.chain.add(new Block(0));






        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(sender);
        executorService.submit(receiver);


        Timer timer = new Timer(10000, e -> {
            try {
                outbox.addMessage(new BlockchainMessage(BlockchainMessageType.SYNC,"test"));
                System.out.println("added new message to outbox");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        timer.start();



    }










}
