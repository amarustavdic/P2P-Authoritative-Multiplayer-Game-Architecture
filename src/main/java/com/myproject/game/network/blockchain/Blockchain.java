package com.myproject.game.network.blockchain;

import com.google.gson.Gson;
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
    private boolean newBlock;
    private BlockchainMessageSender sender;
    private BlockchainMessageReceiver receiver;
    private BlockchainMessageHandler messageHandler;
    private VdfWorker vdfWorker;


    public Blockchain(KademliaDHT dht, int port) {
        this.isBootstrap = Objects.equals(dht.getBootstrapId(), dht.getNodeId());
        this.port = port;
        this.dht = dht;
        this.vdf = new WesolowskiVDF();
        this.chain = new ArrayList<>();
        this.newBlock = Objects.equals(dht.getBootstrapId(), dht.getNodeId());
        this.inbox = new BlockchainInbox();
        this.outbox = new BlockchainOutbox();
        this.messageHandler = new BlockchainMessageHandler(dht,inbox, outbox, chain,4, port, 1000, this);
        this.sender = new BlockchainMessageSender(dht, outbox, port,1000, 4);
        this.receiver = new BlockchainMessageReceiver(port, inbox);
        this.vdfWorker = new VdfWorker(vdf, chain, this, dht, outbox);

        initBlockchain();





        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(sender);
        executorService.submit(receiver);
        executorService.submit(messageHandler);
        executorService.submit(vdfWorker);
    }


    private void initBlockchain() {
        if (isBootstrap) {
            // for simplicity this is going to be done only by the bootstrap node
            // later the vdf setup can be used dynamically in order to improve the security
            // because the security of the vdf depends a lot on the factorization of modulo N
            vdf.setup(2048, "SHA-256");
            // genesis block
            ArrayList<String> consensusList = new ArrayList<>();
            consensusList.add(dht.getNodeId());
            chain.add(new Block(
                    0,
                    vdf.getN(),
                    "0",
                    consensusList,  // list of consensus nodes
                    consensusList,  // shuffled list of consensus nodes
                    dht.getNodeId()
            ));
        } else {
            // send a SYNC request to the closest node, in order to synchronize the chain
            try {
                // just for testing purposes
                Thread.sleep(5000);
                outbox.addMessage(new BlockchainMessage(BlockchainMessageType.SYNC, "hello"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public boolean isNewBlock() {
        return newBlock;
    }

    public void setNewBlock(boolean newBlock) {
        this.newBlock = newBlock;
    }

    public ArrayList<Block> getChain() {
        return chain;
    }
}
