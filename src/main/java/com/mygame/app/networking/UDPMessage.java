package com.mygame.app.networking;

import com.google.gson.Gson;
import com.mygame.app.networking.Node;
import com.mygame.app.networking.UDPProtocol;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UDPMessage {

    private String id;
    private UDPProtocol type;
    private String target;
    private String sender;
    private String receiver;
    private ArrayList<Node> nodes;


    // this constructor is for
    public UDPMessage(String id, UDPProtocol type, String sender, String receiver) {
        this.id = id;
        this.type = type;
        this.target = null;
        this.sender = sender;
        this.receiver = receiver;
        this.nodes = null;
    }


    public byte[] getBytes() {
        return new Gson().toJson(this).getBytes();
    }

    public void print() {
        System.out.println(new Gson().toJson(this));
    }

    public String getReceiver() {
        return receiver;
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
