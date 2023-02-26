package com.mygame.app.networking;

import java.util.ArrayList;
import java.util.List;

public class UDPMessageQueue {

    private static final List<UDPMessage> queue = new ArrayList<UDPMessage>();

    public static void addMessage(UDPMessage message) {
        queue.add(message);
    }

    public static UDPMessage getMessage() {
        UDPMessage message = queue.get(0);
        queue.remove(message);
        return message;
    }

    public static boolean isEmpty() {
        return queue.size() == 0;
    }



}
