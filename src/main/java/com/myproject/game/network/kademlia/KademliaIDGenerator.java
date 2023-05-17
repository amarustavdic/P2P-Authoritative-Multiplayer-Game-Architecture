package com.myproject.game.network.kademlia;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class KademliaIDGenerator {
    private static final int ID_SIZE = 8;       // in bits

    public static KademliaID generateID(String data) {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] dataBytes = data.getBytes();
            byte[] hashBytes = sha1Digest.digest(dataBytes);
            BigInteger hashInt = new BigInteger(1, hashBytes);
            String hexValue = hashInt.toString(16);
            // Truncate or pad the hexadecimal string to the desired length
            hexValue = truncateOrPad(hexValue, ID_SIZE);
            return new KademliaID(hexValue);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate Kademlia ID.", e);
        }
    }

    private static String truncateOrPad(String hexValue, int desiredLength) {
        if (hexValue.length() > desiredLength) {
            // Truncate the string if it's longer than the desired length
            hexValue = hexValue.substring(0, desiredLength);
        } else if (hexValue.length() < desiredLength) {
            // Pad the string with leading zeros if it's shorter than the desired length
            hexValue = String.format("%0" + desiredLength + "x", new BigInteger(hexValue, 16));
        }
        return hexValue;
    }
}
