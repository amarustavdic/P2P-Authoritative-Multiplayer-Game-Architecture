package com.mygame.app.networking.messages;

import com.google.gson.Gson;
import com.mygame.app.networking.Node;
import com.mygame.app.networking.UDPProtocol;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UDPMessage {

    private final UDPMessageHeader header;
    private final UDPMessageBody body;

    public UDPMessage(UDPMessageHeader header, UDPMessageBody body) {
        this.header = header;
        this.body = body;
    }



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
}
