package com.a21vianet.quincysx.library.crypto.transaction;

public class TransactionInput {

    private static final String DEFAULT_SEQUENCE_NUMBER_HEX = "ffffffff";
    private final TransactionOutPoint previousOutput;
    private final int scriptLength;
    private final String signatureScript;
    private final String sequenceNumberHex;

    public TransactionInput(TransactionOutPoint previousOutput, String signatureScript) {
        this(previousOutput, signatureScript.length(), signatureScript, DEFAULT_SEQUENCE_NUMBER_HEX);
    }

    public TransactionInput(TransactionOutPoint previousOutput, int scriptLength, String signatureScript, String sequenceNumberHex) {
        this.previousOutput = previousOutput;
        this.scriptLength = scriptLength;
        this.signatureScript = signatureScript;
        this.sequenceNumberHex = sequenceNumberHex;
    }

    public TransactionOutPoint getPreviousOutput() {
        return previousOutput;
    }

    public int getScriptLength() {
        return scriptLength;
    }

    public String getSignatureScript() {
        return signatureScript;
    }

    public String getSequenceNumberHex() {
        return sequenceNumberHex;
    }
}
