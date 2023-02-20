package com.mygame.app.networking.messages;

import com.mygame.app.networking.Node;

import java.util.List;

public class UDPMessageBody {

    private List<Node> nodes;

    public UDPMessageBody(List<Node> nodes) {
        this.nodes = nodes;
    }


    public List<Node> getNodes() {
        return nodes;
    }
}
