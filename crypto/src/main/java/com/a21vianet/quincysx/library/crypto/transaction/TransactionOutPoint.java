package com.a21vianet.quincysx.library.crypto.transaction;

public class TransactionOutPoint {

    private final String prevTxHash;
    private final int prevTxOutIndex;

    public TransactionOutPoint(final String prevTxHash, final int prevTxOutIndex) {
        this.prevTxHash = prevTxHash;
        this.prevTxOutIndex = prevTxOutIndex;
    }

    public String getPrevTxHash() {
        return prevTxHash;
    }

    public int getPrevTxOutIndex() {
        return prevTxOutIndex;
    }

}
