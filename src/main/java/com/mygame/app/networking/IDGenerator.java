package com.mygame.app.networking;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IDGenerator {

    private static int B;   // ID size in number of bits
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static byte[] id;


    public static void init(int idLength) {
        IDGenerator.B = idLength;
    }

    // data string is going to be nodeIp+port
    public static void generateID(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data.getBytes());
            // Use first B/8 bytes of hash as ID
            byte[] id = new byte[B/8];
            System.arraycopy(hash, 0, id, 0, B/8);
            IDGenerator.id = id;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        //return new BigInteger(bytes).toString(16);

        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0f];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return bytes;
    }

    public static int hexStringToInt(String hex) {
        if (hex == null || hex.isEmpty()) {
            return 0;
        }

        hex = hex.trim().toLowerCase();
        int result = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            if (c >= '0' && c <= '9') {
                result = result * 16 + (c - '0');
            } else if (c >= 'a' && c <= 'f') {
                result = result * 16 + (c - 'a' + 10);
            } else {
                throw new IllegalArgumentException("Invalid hex string: " + hex);
            }
        }
        return result;
    }

    public static String intToHexString(int num) {
        char[] hexChars = new char[8];
        for (int i = 0; i < 8; i++) {
            int nibble = (num >> (28 - i * 4)) & 0xf;
            hexChars[i] = HEX_ARRAY[nibble];
        }
        return new String(hexChars);
    }



    public static String getIdAsString() {
        return bytesToHex(id);
    }
}
