package com.myproject.game.network.blockchain;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class Block {

    private final int blockNumber;
    private final BigInteger modulo;
    private final int vdfDifficulty;
    private String previousBlockHash;
    private String blockHash;
    private ArrayList<String> previousConsensusNodeList;
    private ArrayList<String> shuffledConsensusNodeList;
    private String blockProducer;
    private final long timestamp;
    //private ArrayList<PlayerMatching> transactionData;

    public Block(int blockNumber, BigInteger modulo, String previousBlockHash, ArrayList<String> previousConsensusNodeList, ArrayList<String> shuffledConsensusNodeList, String blockProducer) {
        this.blockNumber = blockNumber;
        this.modulo = modulo;
        this.previousBlockHash = previousBlockHash;
        this.previousConsensusNodeList = previousConsensusNodeList;
        this.shuffledConsensusNodeList = shuffledConsensusNodeList;
        this.blockProducer = blockProducer;
        this.timestamp = Instant.now().getEpochSecond();
        this.vdfDifficulty = 100000;  // for now hardcoded, but can be decided dynamically by the network
        // block hash is calculated last since it includes all the rest of the data of the block in order to be calculated
        this.blockHash = calculateBlockHash();
    }






    private String calculateBlockHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // this still needs to be adjusted later
            String blockData = blockNumber + modulo.toString() + previousBlockHash + previousConsensusNodeList.toString() +
                    shuffledConsensusNodeList.toString() + blockProducer + timestamp + vdfDifficulty;

            byte[] hashBytes = digest.digest(blockData.getBytes());
            BigInteger hashInt = new BigInteger(1, hashBytes);
            StringBuilder hashBuilder = new StringBuilder(hashInt.toString(16));

            while (hashBuilder.length() < 64) {
                hashBuilder.insert(0, "0");
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public BigInteger getModulo() {
        return modulo;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public int getVdfDifficulty() {
        return vdfDifficulty;
    }

    public ArrayList<String> getPreviousConsensusNodeList() {
        return previousConsensusNodeList;
    }

    public ArrayList<String> getShuffledConsensusNodeList() {
        return shuffledConsensusNodeList;
    }


    public int getBlockNumber() {
        return blockNumber;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getBlockProducer() {
        return blockProducer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
