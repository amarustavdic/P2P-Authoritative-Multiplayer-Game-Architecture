package com.mygame.app.networking;
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
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0f];
        }
        return new String(hexChars);
    }



    public static String getIdAsString() {
        return bytesToHex(id);
    }
}
