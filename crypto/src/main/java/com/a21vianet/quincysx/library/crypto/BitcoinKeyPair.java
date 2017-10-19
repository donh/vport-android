package com.a21vianet.quincysx.library.crypto;

public final class BitcoinKeyPair {
    private final String privateKey;
    private final String publicKey;

    public BitcoinKeyPair(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return String.format("[Private key: %s, Public key: %s]", privateKey, publicKey);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        BitcoinKeyPair that = (BitcoinKeyPair) object;
        return privateKey.equals(that.privateKey) && publicKey.equals(that.publicKey);
    }

    @Override
    public int hashCode() {
        int result = privateKey.hashCode();
        result = 31 * result + publicKey.hashCode();
        return result;
    }
}
