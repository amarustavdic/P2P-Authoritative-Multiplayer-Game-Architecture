import java.io.IOException;
import java.net.*;

public class UDPSender extends Thread {

    private final boolean isBootstrapNode;
    private final String bootstrapNodeIp;
    private final int port;

    public UDPSender() {
        this.isBootstrapNode = RoutingTable.isIsBootstrapNode();
        this.bootstrapNodeIp = RoutingTable.getBootstrapNodeIp();
        this.port = RoutingTable.getPort();
    }




    @Override
    public void run() {


        while (true) {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            DatagramSocket ds = null;
            InetAddress ip = null;

            if (!isBootstrapNode && RoutingTable.allBucketsEmpty()) {
                // contact bootstrap node
                try {
                    ip = InetAddress.getByName(bootstrapNodeIp);
                    ds = new DatagramSocket();
                } catch (UnknownHostException | SocketException e) {
                    throw new RuntimeException(e);
                }

                byte buf[] = null;
                String s = "Hello Bootstrap node!";
                buf = s.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!isBootstrapNode && !RoutingTable.allBucketsEmpty()) {
                // contact other nodes the closest ones in routing table
            }

            if (isBootstrapNode && !RoutingTable.allBucketsEmpty()) {
                // if you are a bootstrap node,
                // contact only from routing table if not empty
            }






        }



    }
}
