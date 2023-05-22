package com.myproject.game.network.blockchain;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockchainMessageReceiver implements Runnable {
    private int port;
    private ExecutorService executorService;

    public BlockchainMessageReceiver(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }


    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleMessage(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error in the receiver thread.", e);
        }
    }



    private void handleMessage(Socket clientSocket) {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead > 0) {
                String message = new String(buffer, 0, bytesRead);
                // Process the received message
                processReceivedMessage(message);
            }

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while handling the received message.", e);
        }
    }

    private void processReceivedMessage(String message) {

        System.out.println("Received message: " + message);
    }







}

