import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException {

        RoutingTable.init();
        RoutingTable.setIsBootstrapNode(Boolean.parseBoolean(System.getenv("ARG1")));
        ExecutorService executorService = Executors.newCachedThreadPool();
        UDPReceiver udpReceiver = new UDPReceiver();
        UDPSender udpSender = new UDPSender();
        executorService.submit(udpReceiver);
        executorService.submit(udpSender);



        // for testing purposes only
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("IP address of the machine: " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}
