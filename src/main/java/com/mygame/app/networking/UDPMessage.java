package com.mygame.app.networking;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.ArrayList;

public class UDPMessage {

    private final UDPProtocol type;
    private final String sender_id;
    private final String sender_ip;
    private final String receiver_ip;
    private String target_id;       // can be usefully for getting random player
    private ArrayList<Node> nodes;


    // FIND_NODE / PING / PONG message layout
    public UDPMessage(UDPProtocol type, String sender_id, String sender_ip, String receiver_ip) {
        this.type = type;
        this.sender_id = sender_id;
        this.sender_ip = sender_ip;
        this.receiver_ip = receiver_ip;
    }

    // NODE_FOUND
    public UDPMessage(UDPProtocol type, String sender_id, String sender_ip, String receiver_ip, ArrayList<Node> nodes) {
        this.type = type;
        this.sender_id = sender_id;
        this.sender_ip = sender_ip;
        this.receiver_ip = receiver_ip;
        this.nodes = nodes;
    }




    public Node getNode() {
        boolean boot = false;
        if (sender_id == RoutingTable.getBootstrapNode().getIdHex()) boot = true;
        return new Node(sender_id, sender_ip,5000,boot, Instant.now().getEpochSecond());
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public byte[] getBytes() {
        return new Gson().toJson(this).getBytes();
    }

    public String getSender_ip() {
        return sender_ip;
    }

    public void print() {
        System.out.println(new Gson().toJson(this));
    }

    public String getReceiver_ip() {
        return receiver_ip;
    }

    public UDPProtocol getType() {
        return type;
    }

    public String getSender_id() {
        return sender_id;
    }

    /*
    public void print() {
        Gson gson = new Gson();
        String headerJson = gson.toJson(header);
        String bodyJson = gson.toJson(body);

        String messageJson = "{ \"header\": " + headerJson + ", \"body\": " + bodyJson + " }";
        System.out.println(messageJson);
    }




    public byte[] getBytes() {
        Gson gson = new Gson();
        String headerJson = gson.toJson(header);
        String bodyJson = gson.toJson(body);
        String messageJson = "{ \"header\": " + headerJson + ", \"body\": " + bodyJson + " }";
        return messageJson.getBytes(StandardCharsets.UTF_8);
    }






    public UDPProtocol getProtocol() {
        return header.getProtocol();
    }

    public InetAddress getSenderIP() {
        return header.getSenderIP();
    }

    public InetAddress getReceiverIP() {
        return header.getReceiverIP();
    }

    public int getSenderID() {
        return header.getSenderID();
    }

    public List<Node> getNodes() {
        return body.getNodes();
    }

     */
}
