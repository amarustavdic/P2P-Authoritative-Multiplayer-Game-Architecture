package com.myproject.game.network.kademlia;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KademliaMessage {
    private KademliaMessageType type;
    private String sourceAddress;
    private int sourcePort;
    private String destinationAddress;
    private int destinationPort;
    private String payload;
    private byte[] messageID;

    public KademliaMessage(KademliaMessageType type, String sourceAddress, int sourcePort, String destinationAddress, int destinationPort, String payload) {
        this.type = type;
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
        this.payload = payload;
        this.messageID = generateMessageID();
    }

    private byte[] generateMessageID() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }



    // Getters and Setters

    public KademliaMessageType getType() {
        return type;
    }

    public void setType(KademliaMessageType type) {
        this.type = type;
    }



    public byte[] getMessageID() {
        return messageID;
    }

    public void setMessageID(byte[] messageID) {
        this.messageID = messageID;
    }
}

