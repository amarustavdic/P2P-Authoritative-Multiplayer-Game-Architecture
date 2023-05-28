package com.myproject.game.network.blockchain;

import java.util.ArrayList;

public class MatchRequestList {

    private ArrayList<BlockchainMessage> matchRequests;

    public MatchRequestList() {
        this.matchRequests = new ArrayList<>();
    }

    public void cacheRequest(BlockchainMessage request) {
        matchRequests.add(request);
    }

    public ArrayList<BlockchainMessage> getMatchRequests() {
        return matchRequests;
    }

}
