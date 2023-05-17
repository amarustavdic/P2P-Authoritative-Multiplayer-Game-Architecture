package com.myproject.game.network.kademlia;


import com.google.gson.Gson;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class KademliaMessage {
    private KademliaMessageType type;
    private String srcAddress;
    private int srcPort;
    private String destAddress;
    private int destPort;
    private String payload;
    private String msgID;

    public KademliaMessage(KademliaMessageType type, String srcAddress, int srcPort, String destAddress, int destPort, String payload) {
        this.type = type;
        this.srcAddress = srcAddress;
        this.srcPort = srcPort;
        this.destAddress = destAddress;
        this.destPort = destPort;
        this.payload = payload;
        this.msgID = generateMessageID();
    }

    private String generateMessageID() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            BigInteger hashInteger = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(hashInteger.toString(16));

            // Pad with leading zeros if needed
            while (hexString.length() < 64) {
                hexString.insert(0, "0");
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }




    public String getDestAddress() {
        return destAddress;
    }
}

