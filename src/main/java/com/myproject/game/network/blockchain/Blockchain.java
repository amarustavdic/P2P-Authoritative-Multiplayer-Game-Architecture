package com.myproject.game.network.blockchain;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.myproject.game.ebus.EventType;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.vdf.WesolowskiVDF;
import com.myproject.game.ui.GameScene2;
import com.myproject.game.ui.MatchmakingScene;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Blockchain {

    private final EventBus eventBus;
    private final boolean isBootstrap;
    private final int port;
    private final KademliaDHT dht;
    private final WesolowskiVDF vdf;
    private final BlockchainOutbox outbox;
    private final BlockchainInbox inbox;
    private final InclusionRequestsList inclusionRequestsList;
    private final MatchRequestList matchRequestList;
    private final ArrayList<Block> chain;
    private boolean newBlock;
    private final BlockchainMessageSender sender;
    private final BlockchainMessageReceiver receiver;
    private final BlockchainMessageHandler messageHandler;
    private final VDFService VDFService;


    public Blockchain(KademliaDHT dht, int port, EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.register(this);

        this.isBootstrap = Objects.equals(dht.getBootstrapId(), dht.getNodeId());
        this.port = port;
        this.dht = dht;
        this.vdf = new WesolowskiVDF();
        this.chain = new ArrayList<>();
        this.newBlock = true;
        this.inbox = new BlockchainInbox();
        this.outbox = new BlockchainOutbox();
        this.matchRequestList = new MatchRequestList();
        this.inclusionRequestsList = new InclusionRequestsList();
        this.VDFService = new VDFService(eventBus,matchRequestList,inclusionRequestsList, vdf, this, dht, outbox);
        this.messageHandler = new BlockchainMessageHandler(eventBus,matchRequestList,inclusionRequestsList, VDFService,dht,inbox, outbox, chain,4, port, 1000, this);
        this.sender = new BlockchainMessageSender(dht, outbox, port,1000, 4);
        this.receiver = new BlockchainMessageReceiver(port, inbox);



        initBlockchain();





        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(VDFService);
        executorService.submit(sender);
        executorService.submit(receiver);
        executorService.submit(messageHandler);

    }


    @Subscribe
    public void handleMessage(EventType eventType) {

        if (eventType == EventType.ONLINE_GAME) {
            System.out.println("im from blockchain");
            this.makeMatchmakingRequest(BlockchainMessageType.TTT_MATCHMAKING_REQUEST, dht.getNodeId());
        }
    }









    private void initBlockchain() {
        if (isBootstrap) {
            vdf.setup(2048, "SHA-256");
            // genesis block
            ArrayList<String> consensusList = new ArrayList<>();
            consensusList.add(dht.getNodeId());
            chain.add(new Block(
                    0,
                    vdf.getN(),
                    "0",
                    consensusList,  // list of consensus nodes
                    dht.getNodeId(),
                    null,
                    null
            ));
        } else {
            // send a SYNC request to the closest node, in order to synchronize the chain
            // jebem si mater
            vdf.setup(2048, "SHA-256");
            BlockchainMessage inclusionRequest = new BlockchainMessage(
                    BlockchainMessageType.INCLUSION_REQUEST,
                    dht.getNodeId()
            );
            try {
                outbox.addMessage(inclusionRequest);
                System.out.println("inclusion request was added to the outbox");
            } catch (InterruptedException e) {
                System.out.println("inclusion request was not added to the outbox");

            }
        }

    }


    public void makeMatchmakingRequest(BlockchainMessageType requestType, String myID) {
        BlockchainMessage request = new BlockchainMessage(
                requestType,
                myID
        );
        try {
            outbox.addMessage(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    public boolean isChainEmpty() {
        return chain.isEmpty();
    }

    public void addNewBlock(Block block) {
        chain.add(block);
    }

    public Block getLastBlock() {
        return chain.get(chain.size()-1);
    }
}
