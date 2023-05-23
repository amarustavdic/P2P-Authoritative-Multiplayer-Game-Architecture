package com.myproject.game.network.vdf;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

public class WesolowskiVDF {
    private String hashAlgorithm;
    private BigInteger N;


    // VDF(setup, eval, verify)
    /*
     *         lambda   - RSA security usually 2048 for high security
     *  hashAlgorithm   - hash function security parameter
     */
    public void setup(int lambda, String hashAlgorithm) {
        switch (hashAlgorithm) {
            case "SHA-256":
            case "SHA-512":
            case "SHA3-256":
            case "SHA3-512":
                this.hashAlgorithm = hashAlgorithm;
                break;
            default:
                throw new RuntimeException(hashAlgorithm + " not supported");
        }

        SecureRandom rand = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(lambda / 2, rand);
        BigInteger q = BigInteger.probablePrime(lambda / 2, rand);
        N = p.multiply(q);
    }

    public EvalResult eval(byte[] m, int T, BigInteger N) {
        BigInteger x = new BigInteger(hash(m));
        BigInteger y = x;
        for (int i = 0; i < T; i++) {
            y = y.modPow(BigInteger.TWO, N);
        }
        BigInteger l = hashPrime((x.add(y)).toByteArray());
        BigInteger proof = x.modPow(BigInteger.TWO.pow(T).divide(l), N);
        return new EvalResult(proof, l);
    }

    public boolean verify(byte[] m, int T, BigInteger l, BigInteger proof) {
        BigInteger x = new BigInteger(hash(m));
        BigInteger r = BigInteger.TWO.modPow(BigInteger.valueOf(T),l);
        BigInteger y = modExp(proof,x,l,r,N);
        BigInteger xPlusY = x.add(y);
        return Objects.equals(l, hashPrime(xPlusY.toByteArray()));
    }



    // HELPER FUNCTIONS BELLOW
    private byte[] hash(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private BigInteger hashPrime(byte[] m) {
        byte[] hash = hash(m);
        BigInteger hashVal = new BigInteger(1, hash);
        return hashVal.abs().nextProbablePrime();
    }

    private BigInteger modExp(BigInteger x, BigInteger y, BigInteger a, BigInteger b, BigInteger N) {
        int h = Math.max(a.bitLength() + 1, b.bitLength() + 1);
        BigInteger z = BigInteger.ONE;
        BigInteger q = x.multiply(y).mod(N);
        for (int i = h - 1; i >= 0; i--) {
            z = z.multiply(z).mod(N);
            if (a.testBit(i) && !b.testBit(i)) {
                z = z.multiply(x).mod(N);
            } else if (!a.testBit(i) && b.testBit(i)) {
                z = z.multiply(y).mod(N);
            } else if (a.testBit(i) && b.testBit(i)) {
                z = z.multiply(q).mod(N);
            }
        }
        return z;
    }



    // getters bellow
    public BigInteger getN() {
        return N;
    }
}