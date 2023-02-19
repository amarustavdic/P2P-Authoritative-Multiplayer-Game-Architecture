import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException {
        // for testing purposes only
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println(Constants.INFO + "IP address of the machine: " + ip.getHostAddress() + Constants.RESET);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }




        RoutingTable.init();
        ExecutorService executorService = Executors.newCachedThreadPool();
        UDPReceiver udpReceiver = new UDPReceiver();
        UDPSender udpSender = new UDPSender();
        RoutingTableUpdater RTUpdater = new RoutingTableUpdater();

        executorService.submit(udpReceiver);
        executorService.submit(udpSender);
        executorService.submit(RTUpdater);



    }
}
