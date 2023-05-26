package com.myproject.game.network.blockchain;

import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.vdf.EvalResult;
import com.myproject.game.network.vdf.WesolowskiVDF;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Blockchain {

    private int port;
    private KademliaDHT dht;
    private WesolowskiVDF vdf;
    private BlockchainOutbox outbox;
    private BlockchainInbox inbox;
    private ArrayList<Block> chain;
    private BlockchainMessageSender sender;
    private BlockchainMessageReceiver receiver;


    public Blockchain(KademliaDHT dht, int port) {
        this.port = port;
        this.dht = dht;
        this.vdf = new WesolowskiVDF();
        this.chain = new ArrayList<>();
        this.inbox = new BlockchainInbox();
        this.outbox = new BlockchainOutbox();
        this.sender = new BlockchainMessageSender(dht, outbox, port,1000, 2);
        this.receiver = new BlockchainMessageReceiver(port);


        // setup is going to be done only once at the beginning
        // it could be performed dynamically in order to improve security, by changing the modulo N
        vdf.setup(2048, "SHA-512");


        // genesis block hardcoded
        chain.add(new Block(0, vdf.getN(),0));




        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(sender);
        executorService.submit(receiver);
        executorService.submit(new VdfWorker(vdf));









    }










}
