package com.mygame.app.networking.messages;

import com.mygame.app.networking.UDPProtocol;

import java.net.InetAddress;


public class UDPMessageHeader {

    private UDPProtocol protocol;
    private InetAddress senderIP;
    private InetAddress receiverIP;
    private int senderID;


    public UDPMessageHeader(
            UDPProtocol protocol,
            InetAddress senderIP,
            InetAddress receiverIP,
            int senderID) {

        this.protocol = protocol;
        this.senderIP = senderIP;
        this.receiverIP = receiverIP;
        this.senderID = senderID;
    }


    public UDPProtocol getProtocol() {
        return protocol;
    }

    public InetAddress getSenderIP() {
        return senderIP;
    }

    public InetAddress getReceiverIP() {
        return receiverIP;
    }

    public int getSenderID() {
        return senderID;
    }
}
