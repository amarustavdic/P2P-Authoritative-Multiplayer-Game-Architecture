package com.myproject.game.network.blockchain;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class BlockchainMessage {
    private final BlockchainMessageType type;
    private final String payload;
    private final String msgID;


    public BlockchainMessage(BlockchainMessageType type, String payload) {
        this.type = type;
        this.payload = payload;
        this.msgID = "0";
    }


    public BlockchainMessageType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public String getMsgID() {
        return msgID;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String generateHashID() {
        try {
            String input = type.toString() + payload;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes());

            String hashID = Base64.getEncoder().encodeToString(hash);

            return hashID;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "0";
    }

}
