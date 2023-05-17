package com.myproject.game.network.kademlia;

import java.math.BigInteger;

public class KademliaID {
    private final String id;  // hex value is used

    public KademliaID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public BigInteger getNumericID() {
        return new BigInteger(id, 16);
    }

    public int getDistance(KademliaID other) {
        BigInteger distance = getNumericID().xor(other.getNumericID());
        return distance.bitLength();
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        KademliaID other = (KademliaID) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
