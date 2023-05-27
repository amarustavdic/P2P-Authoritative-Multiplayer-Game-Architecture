package com.myproject.game.network.blockchain;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockchainMessageReceiver implements Runnable {
    private int port;
    private Gson gson;
    private BlockchainInbox inbox;
    private ExecutorService executorService;

    public BlockchainMessageReceiver(int port, BlockchainInbox inbox) {
        this.port = port;
        this.inbox = inbox;
        this.gson = new Gson();
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
                String jsonMessage = new String(buffer, 0, bytesRead);
                // Process the received message
                BlockchainMessage blockchainMessage = gson.fromJson(jsonMessage, BlockchainMessage.class);
                inbox.addMessage(blockchainMessage);
            }

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while handling the received message.", e);
        } catch (InterruptedException e) {
            System.out.println("Unable to add new message to inbox queue!");
            throw new RuntimeException(e);
        }
    }








}

