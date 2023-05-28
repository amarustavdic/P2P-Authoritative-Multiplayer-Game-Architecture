package com.myproject.game.network.vdf;

import com.google.gson.Gson;

import java.math.BigInteger;

public class VDFResult {
    private final BigInteger proof;
    private final BigInteger lPrime;

    public VDFResult(BigInteger proof, BigInteger lPrime) {
        this.proof = proof;
        this.lPrime = lPrime;
    }

    public BigInteger getProof() {
        return proof;
    }

    public BigInteger getLPrime() {
        return lPrime;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}