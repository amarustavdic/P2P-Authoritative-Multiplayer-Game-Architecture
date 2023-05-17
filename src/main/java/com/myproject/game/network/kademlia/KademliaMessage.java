package com.myproject.game.network.kademlia;


import com.google.gson.Gson;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class KademliaMessage {
    private KademliaMessageType type;
    private KademliaID srcId;
    private String srcAddress;
    private int srcPort;
    private KademliaID destId;
    private String destAddress;
    private int destPort;
    private String payload;
    private String msgID;

    public KademliaMessage(KademliaMessageType type, KademliaID srcId, String srcAddress, int srcPort, KademliaID destId, String destAddress, int destPort, String payload) {
        this.type = type;
        this.srcId = srcId;
        this.srcAddress = srcAddress;
        this.srcPort = srcPort;
        this.destId = destId;
        this.destAddress = destAddress;
        this.destPort = destPort;
        this.payload = payload;
        this.msgID = generateMessageID();
    }

    public KademliaMessage(KademliaMessageType type, KademliaID srcId, String srcAddress, int srcPort, KademliaID destId, String destAddress, int destPort, ArrayList<Node> nodes) {
        this.type = type;
        this.srcId = srcId;
        this.srcAddress = srcAddress;
        this.srcPort = srcPort;
        this.destId = destId;
        this.destAddress = destAddress;
        this.destPort = destPort;
        this.msgID = generateMessageID();

        Gson gson = new Gson();
        // Convert ArrayList to JSON
        this.payload = gson.toJson(nodes);
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

    public KademliaMessageType getType() {
        return type;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public int getDestPort() {
        return destPort;
    }

    public String getPayload() {
        return payload;
    }

    public String getMsgID() {
        return msgID;
    }

    public KademliaID getSrcId() {
        return srcId;
    }

    public KademliaID getDestId() {
        return destId;
    }
}

