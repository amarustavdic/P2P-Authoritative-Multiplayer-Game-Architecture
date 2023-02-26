package com.mygame.app.networking;

import java.util.ArrayList;

public class PingedList {


    public static ArrayList<Node> pinged = new ArrayList<>();

    public static void removeNode(String nodeId) {
        for (int i = 0; i < pinged.size(); i++) {
            if (pinged.get(i).getIdHex().equals(nodeId)) {
                pinged.remove(i);
                return;
            }
        }
    }
}
