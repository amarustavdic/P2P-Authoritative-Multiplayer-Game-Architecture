import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReceiver extends Thread {

    private DatagramSocket udpSocket;
    private byte[] inBuff;
    private Gson gson;

    public UDPReceiver() throws SocketException {
        this.udpSocket = new DatagramSocket(RoutingTable.getPort());
        this.gson = new Gson();
        this.inBuff = new byte[65535];
    }


    @Override
    public void run() {

        DatagramPacket DpReceive = null;
        while (true) {
            DpReceive = new DatagramPacket(inBuff, inBuff.length);
            try {
                udpSocket.receive(DpReceive);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(data(inBuff));
            String jsonStr = String.valueOf(data(inBuff));
            UDPMessage message = new Gson().fromJson(jsonStr, UDPMessage.class);

            UDPMessage msg = new UDPMessage(UDPProtocol.PONG, message.getData(), message.getReceiverIP(), message.getSenderIP());
            UDPMessageQueue.addMessage(msg);

            // Clear the buffer after every message.
            inBuff = new byte[65535];
        }

    }

    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}

