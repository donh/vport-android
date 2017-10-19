package com.a21vianet.quincysx.library.crypto.messagedigesters;

public enum MessageDigestAlgorithm {

    SHA_256("sha-256"),
    RIPEMD_160("ripemd160");

    private final String algorithm;

    MessageDigestAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return algorithm;
    }
}
