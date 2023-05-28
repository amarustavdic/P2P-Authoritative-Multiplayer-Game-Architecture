package com.myproject.game.network.blockchain;

import com.google.gson.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;



public class BlockchainMessageReceiver implements Runnable {
    private int port;
    private Gson gson;
    private BlockchainInbox inbox;
    private AsynchronousServerSocketChannel serverSocketChannel;

    public BlockchainMessageReceiver(int port, BlockchainInbox inbox) {
        this.port = port;
        this.inbox = inbox;
        this.gson = new Gson();
        try {
            this.serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        serverSocketChannel.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel clientSocketChannel, Object attachment) {
                serverSocketChannel.accept(null, this); // Accept the next connection
                handleMessage(clientSocketChannel);
                System.out.println("New message has been received");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                throw new RuntimeException("Error in accepting client connection.", exc);
            }
        });
    }

    private void handleMessage(AsynchronousSocketChannel clientSocketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        clientSocketChannel.read(buffer, null, new CompletionHandler<>() {
            @Override
            public void completed(Integer bytesRead, Object attachment) {
                if (bytesRead > 0) {
                    buffer.flip();
                    String jsonMessage = new String(buffer.array(), 0, bytesRead, StandardCharsets.UTF_8);
                    // Process the received message


                    System.out.println(jsonMessage);


                    BlockchainMessage blockchainMessage = gson.fromJson(jsonMessage, BlockchainMessage.class);


                    try {
                        inbox.addMessage(blockchainMessage);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Clear the buffer and prepare for the next read
                buffer.clear();
                clientSocketChannel.read(buffer, null, this);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                try {
                    clientSocketChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error while closing client socket channel.", e);
                }
            }
        });
    }

}
