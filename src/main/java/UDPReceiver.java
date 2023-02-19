import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReceiver extends Thread {


    @Override
    public void run() {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(5000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] receive = new byte[65535];

        DatagramPacket DpReceive = null;
        while (true) {
            DpReceive = new DatagramPacket(receive, receive.length);
            try {
                ds.receive(DpReceive);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(data(receive));


            // Clear the buffer after every message.
            receive = new byte[65535];
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

