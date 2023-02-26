package com.mygame.app.networking;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.ArrayList;

public class UDPMessage {

    private String id;
    private UDPProtocol type;
    private String target;
    private String sender;
    private String receiver;
    private ArrayList<Node> nodes;


    // this constructor is for ... lets say regular message
    public UDPMessage(String id, UDPProtocol type, String sender, String receiver) {
        this.id = id;
        this.type = type;
        this.target = null;
        this.sender = sender;
        this.receiver = receiver;
        this.nodes = null;
    }

    public UDPMessage(String id, UDPProtocol type, String sender, String receiver, ArrayList<Node> nodes) {
        this.id = id;
        this.type = type;
        this.target = null;
        this.sender = sender;
        this.receiver = receiver;
        this.nodes = nodes;
    }


    public Node getNode() {
        boolean boot = false;
        if (id == RoutingTable.getBootstrapNode().getIdHex()) boot = true;
        return new Node(id,sender,5000,boot, Instant.now().getEpochSecond());
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public byte[] getBytes() {
        return new Gson().toJson(this).getBytes();
    }

    public String getSender() {
        return sender;
    }

    public void print() {
        System.out.println(new Gson().toJson(this));
    }

    public String getReceiver() {
        return receiver;
    }

    public UDPProtocol getType() {
        return type;
    }

    public String getId() {
        return id;
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
