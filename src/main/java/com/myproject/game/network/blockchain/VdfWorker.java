package com.myproject.game.network.blockchain;

import com.myproject.game.network.kademlia.KademliaDHT;
import com.myproject.game.network.vdf.EvalResult;
import com.myproject.game.network.vdf.WesolowskiVDF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class VdfWorker implements Runnable {
    private WesolowskiVDF vdf;
    private ArrayList<Block> chain;
    private Blockchain blockchain;
    private BlockchainOutbox outbox;
    private KademliaDHT dht;

    public VdfWorker(WesolowskiVDF vdf, ArrayList<Block> chain, Blockchain blockchain, KademliaDHT dht, BlockchainOutbox outbox) {
        this.vdf = vdf;
        this.chain = chain;
        this.blockchain = blockchain;
        this.dht = dht;
        this.outbox = outbox;
    }



    @Override
    public void run() {

        while (true) {
            if (blockchain.isNewBlock()) {
                blockchain.setNewBlock(false);
                Block lastBlock = chain.get(chain.size()-1);   // getting the last block from the chain
                // data that is necessary for the vdf evaluation
                // - hash of the last block
                // - vdf difficulty that is stored on the last block in chain
                // - modulo N, also stored in the last block of chain
                // all the data necessary for the calculation should be retreaded from the chain
                EvalResult result = vdf.eval(
                        lastBlock.getBlockHash().getBytes(),
                        lastBlock.getVdfDifficulty(),
                        lastBlock.getModulo()
                );

                // shuffle the list of consensus nodes
                ArrayList<String> consensusList = lastBlock.getPreviousConsensusNodeList();

                // uses result of the VDF as a seed for producing the random shuffle
                long seed = result.getLPrime().longValue();
                Collections.shuffle(consensusList, new Random(seed));


                System.out.println("Node ID: " + dht.getNodeId());

                // Print the shuffled ArrayList
                System.out.println("Shuffled ArrayList bla bla: " + consensusList);



                if (Objects.equals(consensusList.get(0), dht.getNodeId())) {
                    System.out.println("Im the one");

                    // make a new block
                    Block newBlock = new Block(
                            lastBlock.getBlockNumber()+1,
                            lastBlock.getModulo(),
                            lastBlock.getBlockHash(),

                            // here we can also include the new nodes that have sent the inclusion request
                            // to join the consensus list
                            lastBlock.getPreviousConsensusNodeList(),
                            consensusList,
                            dht.getNodeId()
                    );
                    // broadcast the new block
                    chain.add(newBlock);

                    BlockchainMessage message = new BlockchainMessage(
                            BlockchainMessageType.NEW_BLOCK,
                            newBlock.toJson()
                    );

                    try {
                        outbox.addMessage(message);
                        System.out.println("added to the outbox");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    blockchain.setNewBlock(true);
                }



                // then the voting can be done here, but it is being skipped for now
            }
        }
    }
}
