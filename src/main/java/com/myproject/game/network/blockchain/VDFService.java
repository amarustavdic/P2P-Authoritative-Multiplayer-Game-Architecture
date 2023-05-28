package com.myproject.game.network.blockchain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.vdf.VDFResult;
import com.myproject.game.network.vdf.WesolowskiVDF;

import java.math.BigInteger;
import java.util.*;

public class VDFService implements Runnable {
    private final Gson gson;
    private final WesolowskiVDF vdf;
    private final Blockchain blockchain;
    private final BlockchainOutbox outbox;
    private final KademliaDHT dht;
    private final InclusionRequestsList inclusionRequestsList;
    private final MatchRequestList requestList;

    public VDFService(MatchRequestList requestList,InclusionRequestsList inclusionRequestsList, WesolowskiVDF vdf, Blockchain blockchain, KademliaDHT dht, BlockchainOutbox outbox) {
        this.gson = new Gson();
        this.vdf = vdf;
        this.dht = dht;
        this.blockchain = blockchain;
        this.outbox = outbox;
        this.inclusionRequestsList = inclusionRequestsList;
        this.requestList = requestList;
    }



    @Override
    public void run() {

        while (true) {
            // only if chain is not empty
            System.out.println(blockchain.getChain().size());
            if (!blockchain.isChainEmpty()) {
                Block lastblock = blockchain.getLastBlock();     // get the last block in the chain
                System.out.println("section 1");
                // evaluating VDF based on parameters taken from the last block
                VDFResult vdfResult = vdf.eval(
                        lastblock.getBlockHash().getBytes(),
                        lastblock.getVdfDifficulty(),
                        lastblock.getModulo()
                );
                System.out.println("section 2");
                System.out.println(vdfResult.toJson());

                // getting next producer ID after shuffle
                String nextProducerID = getBlockProducerID(
                        lastblock.getPreviousConsensusNodeList(),
                        vdfResult.getLPrime().intValue()
                );


                if (dht.getNodeId().equals(nextProducerID)) {
                    System.out.println("I am the next block producer");
                    Block newBlock;
                    if (requestList.getMatchRequests().isEmpty()) {
                        newBlock = new Block(
                                lastblock.getBlockNumber()+1,
                                lastblock.getModulo(),
                                lastblock.getBlockHash(),
                                inclusionRequestsIncluded(lastblock.getPreviousConsensusNodeList()),
                                dht.getNodeId(),
                                null,
                                null
                        );
                    } else {
                        String[] ps = matchPlayers().split(",");

                        newBlock = new Block(
                                lastblock.getBlockNumber()+1,
                                lastblock.getModulo(),
                                lastblock.getBlockHash(),
                                inclusionRequestsIncluded(lastblock.getPreviousConsensusNodeList()),
                                dht.getNodeId(),
                                ps[0],
                                ps[1]
                        );
                    }



                    System.out.println(newBlock.toJson());
                    processNewlyCreatedBlock(newBlock);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private String matchPlayers() {
        StringBuilder s = new StringBuilder();
        int cnt = 0;
        for (BlockchainMessage request : requestList.getMatchRequests()) {
            cnt++;
            if (cnt > 2) break;
            if (request.getType().equals(BlockchainMessageType.TTT_MATCHMAKING_REQUEST)) {
                s.append(request.getPayload()).append(",");
            }
        }
        return s.toString();
    }











    // before including the node in the consensus list, later on
    // PoS (proof of stake) could be used, to improve security and scalability
    // for example, in game money could be used
    private ArrayList<String> inclusionRequestsIncluded(ArrayList<String> consensus) {

        ArrayList<BlockchainMessage> inclusionsRequests = inclusionRequestsList.getInclusionRequests();
        for (BlockchainMessage message : inclusionsRequests) {
            String nodeId = message.getPayload();
            if (!consensus.contains(nodeId)) {
                consensus.add(nodeId);
            }
        }
        inclusionsRequests.clear();
        inclusionRequestsList.clearInclusionRequest();
        return consensus;
    }



    // returning the ID of next block producer
    private String getBlockProducerID(ArrayList<String> consensusList, int seed) {
        Collections.shuffle(consensusList, new Random(seed));
        return consensusList.get(0);
    }

    // process newly created block
    private void processNewlyCreatedBlock(Block block) {
        blockchain.addNewBlock(block);

        // serialize new block, and push it to outbox queue
        BlockchainMessage message = new BlockchainMessage(
                BlockchainMessageType.NEW_BLOCK,
                block.toJson()
        );

        // push message to outbox queue
        try {
            outbox.addMessage(message);
        } catch (InterruptedException e) {
            System.out.println("Failed to add newly built block to the outbox!");
        }
    }
}
