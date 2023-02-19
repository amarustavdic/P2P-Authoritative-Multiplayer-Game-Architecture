import com.google.gson.Gson;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPMessage {

    private UDPProtocol protocol;
    private InetAddress senderIP;
    private InetAddress receiverIP;
    private String data;


    public UDPMessage(UDPProtocol protocol, String data, InetAddress senderIP, InetAddress receiverIP) {
        this.protocol = protocol;
        this.senderIP = senderIP;
        this.receiverIP = receiverIP;
        this.data = data;
    }



    public void print() {
        System.out.println(new Gson().toJson(this));
    }

    public void printProtocol() {
        System.out.println(protocol);
    }

    public byte[] getBytes() {
        return new Gson().toJson(this).getBytes(StandardCharsets.UTF_8);
    }


    public UDPProtocol getProtocol() {
        return protocol;
    }

    public String getData() {
        return data;
    }

    public InetAddress getSenderIP() {
        return senderIP;
    }

    public InetAddress getReceiverIP() {
        return receiverIP;
    }
}
