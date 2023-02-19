import java.io.*;
import java.net.DatagramSocket;
import java.net.Socket;


public class Client extends Thread {

    private final Socket tcpConnection;
    private final DatagramSocket udpConnection;
    private BufferedReader in;
    private BufferedWriter out;
    private final int ID;


    public Client(Socket connection) {
        this.tcpConnection = connection;
        this.udpConnection = null;
        this.ID = RoutingTable.getLocalID();
        try {
            in  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(DatagramSocket udpConnection) {
        this.tcpConnection = null;
        this.udpConnection = udpConnection;
        this.ID = RoutingTable.getLocalID();
        in  = null; //new BufferedReader(new InputStreamReader(udpConnection));
        out = null; //new BufferedWriter(new OutputStreamWriter(udpConnection.getOutputStream()));
    }

    @Override
    public void run() {
        System.out.println();
        System.out.println("Connection established with: "
                + String.valueOf(tcpConnection.getInetAddress()).split("/")[1]);

        String line;
        try {
            while ((line = in.readLine()) != null) {
                String host = tcpConnection.getInetAddress().getHostAddress();
            }

        } catch (Exception e) {
            try {
                in.close();
                out.close();
                tcpConnection.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public Socket getConnection() {
        return tcpConnection;
    }

    public int getID() {
        return ID;
    }
}
