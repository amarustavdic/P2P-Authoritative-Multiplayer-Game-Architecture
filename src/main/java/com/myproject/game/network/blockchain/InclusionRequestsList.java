package com.myproject.game.network.blockchain;

import java.util.ArrayList;



public class InclusionRequestsList {
    private ArrayList<BlockchainMessage> inclusionRequests;


    public InclusionRequestsList() {
        this.inclusionRequests = new ArrayList<>();
    }


    public void cacheNewInclusionRequest(BlockchainMessage message) {
        inclusionRequests.add(message);
    }

    public ArrayList<BlockchainMessage> getInclusionRequests() {
        return inclusionRequests;
    }

    public void clearInclusionRequest() {
        inclusionRequests.clear();
    }



}
