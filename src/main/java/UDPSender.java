import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;

public class UDPSender extends Thread {

    private final boolean isBootstrapNode;
    private final InetAddress bootstrapNodeIp;
    private final int port;
    private final InetAddress senderIP;
    private byte[] outBuf;
    private DatagramSocket udpSocket;
    private Gson gson;

    public UDPSender() throws UnknownHostException, SocketException {
        this.isBootstrapNode = RoutingTable.isIsBootstrapNode();
        this.bootstrapNodeIp = InetAddress.getByName(RoutingTable.getBootstrapNodeIp());
        this.port = RoutingTable.getPort();
        this.gson = new Gson();
        this.senderIP = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        this.udpSocket = new DatagramSocket();
    }




    @Override
    public void run() {


        while (true) {
            // for testing purposes to see easier what is happening
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!UDPMessageQueue.isEmpty()) {
                UDPMessage msg = UDPMessageQueue.getMessage();
                outBuf = msg.getBytes();

                DatagramPacket packet = new DatagramPacket(outBuf, outBuf.length, msg.getReceiverIP(), port);
                try {
                    udpSocket.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
