import java.io.IOException;
import java.net.*;

public class UDPSender extends Thread {

    private byte[] outBuf;
    private DatagramSocket udpSocket;

    public UDPSender() throws SocketException {
        this.udpSocket = new DatagramSocket();
    }


    @Override
    public void run() {
        while (true) {
            // for testing purposes to see easier what is happening
            try {
                sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!UDPMessageQueue.isEmpty()) {
                UDPMessage msg = UDPMessageQueue.getMessage();
                outBuf = msg.getBytes();

                DatagramPacket packet = new DatagramPacket(
                        outBuf,
                        outBuf.length,
                        msg.getReceiverIP(),
                        5000
                );
                try {
                    udpSocket.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Message sent: ");
                msg.print();
            }
        }
    }
}
