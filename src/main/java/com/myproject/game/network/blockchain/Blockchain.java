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

    private boolean isBootstrap;
    private int port;
    private KademliaDHT dht;
    private WesolowskiVDF vdf;
    private BlockchainOutbox outbox;
    private BlockchainInbox inbox;
    private ArrayList<Block> chain;
    private BlockchainMessageSender sender;
    private BlockchainMessageReceiver receiver;
    private BlockchainMessageHandler messageHandler;
    private VdfWorker vdfWorker;


    public Blockchain(KademliaDHT dht, int port, boolean isBootstrap) {
        this.isBootstrap = isBootstrap;
        this.port = port;
        this.dht = dht;
        this.vdf = new WesolowskiVDF();
        this.chain = new ArrayList<>();
        this.inbox = new BlockchainInbox();
        this.outbox = new BlockchainOutbox();
        this.messageHandler = new BlockchainMessageHandler(dht,inbox, outbox);
        this.sender = new BlockchainMessageSender(dht, outbox, port,1000, 2);
        this.receiver = new BlockchainMessageReceiver(port, inbox);
        this.vdfWorker = new VdfWorker(vdf, chain);

        initBlockchain();



        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(vdfWorker);
        executorService.submit(sender);
        executorService.submit(receiver);
        executorService.submit(messageHandler);
    }


    private void initBlockchain() {
        if (isBootstrap) {
            // for simplicity this is going to be done only by the bootstrap node
            // later the vdf setup can be used dynamically in order to improve the security
            // because the security of the vdf depends a lot on the factorization of modulo N
            vdf.setup(2048, "SHA-256");
            // genesis block
            chain.add(new Block(
                    0,
                    vdf.getN(),
                    "0",
                    new ArrayList<>(),
                    new ArrayList<>(),
                    dht.getNodeId()
            ));
        } else {
            // send a SYNC request to the closest node, in order to synchronize the chain
            try {
                outbox.addMessage(new BlockchainMessage(BlockchainMessageType.SYNC, "hello"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }









}
