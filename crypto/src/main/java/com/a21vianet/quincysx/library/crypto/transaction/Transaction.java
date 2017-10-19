package com.a21vianet.quincysx.library.crypto.transaction;


import com.a21vianet.quincysx.library.crypto.converters.EndianConverter;
import com.a21vianet.quincysx.library.crypto.converters.SatoshiConverter;
import com.a21vianet.quincysx.library.crypto.encoders.HexEncoder;

import java.math.BigInteger;
import java.util.List;

public class Transaction {

    private static final int DEFAULT_VERSION = 1;
    private static final int DEFAULT_LOCK_TIME = 0;
    private static final int HASH_CODE_TYPE = 1;

    private final boolean isSigned;
    private final int version;
    private final int inputCount;
    private final int outputCount;
    private final List<TransactionInput> transactionInputs;
    private final List<TransactionOutput> transactionOutputs;
    private final int lockTime;
    private final int hashCodeType;

    public Transaction(final boolean isSigned,
                       final List<TransactionInput> transactionInputs,
                       final List<TransactionOutput> transactionOutputs) {
        this(
                isSigned,
                DEFAULT_VERSION,
                transactionInputs.size(),
                transactionOutputs.size(),
                transactionInputs,
                transactionOutputs,
                DEFAULT_LOCK_TIME,
                HASH_CODE_TYPE
        );
    }

    public Transaction(final boolean isSigned,
                       final int version,
                       final int inputCount, final int outputCount,
                       final List<TransactionInput> transactionInputs, final List<TransactionOutput> transactionOutputs,
                       final int lockTime, final int hashCodeType) {
        this.isSigned = isSigned;
        this.version = version;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.transactionInputs = transactionInputs;
        this.transactionOutputs = transactionOutputs;
        this.lockTime = lockTime;
        this.hashCodeType = hashCodeType;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public String toHexString() {
        String versionHex = HexEncoder.encode(version, 4);
        String versionHexLittleEndian = EndianConverter.switchEndianness(versionHex);
        String inputCountHex = HexEncoder.encode(inputCount, 1); // todo: 1 - 9 bytes
        String outputCountHex = HexEncoder.encode(outputCount, 1); // todo: 1 - 9 bytes
        String lockTimeHex = HexEncoder.encode(lockTime, 4);
        String lockTimeHexLittleEndian = EndianConverter.switchEndianness(lockTimeHex);

        String prevTxHashLittleEndian = EndianConverter.switchEndianness(transactionInputs.get(0).getPreviousOutput().getPrevTxHash());
        String prevTxOutIndexHex = HexEncoder.encode(transactionInputs.get(0).getPreviousOutput().getPrevTxOutIndex(), 4);
        String prevTxOutIndexHexLittleEndian = EndianConverter.switchEndianness(prevTxOutIndexHex);
        String inputScriptLength = HexEncoder.encode(transactionInputs.get(0).getScriptLength() / 2, 1); // todo: 1 - 9 bytes
        BigInteger valueInSatoshi = SatoshiConverter.btcToSatoshi(transactionOutputs.get(0).getValue());
        String valueInSatoshiHex = HexEncoder.encode(valueInSatoshi, 8);
        String valueInSatoshiHexLittleEndian = EndianConverter.switchEndianness(valueInSatoshiHex);
        String pkScriptLength = HexEncoder.encode(transactionOutputs.get(0).getPkScriptLength() / 2, 1); // todo: 1 - 9 bytes

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(versionHexLittleEndian)
                .append(inputCountHex)
                .append(prevTxHashLittleEndian)
                .append(prevTxOutIndexHexLittleEndian)
                .append(inputScriptLength)
                .append(transactionInputs.get(0).getSignatureScript())
                .append(transactionInputs.get(0).getSequenceNumberHex())
                .append(outputCountHex)
                .append(valueInSatoshiHexLittleEndian)
                .append(pkScriptLength)
                .append(transactionOutputs.get(0).getPkScript())
                .append(lockTimeHexLittleEndian);

        // add hash code type only to the unsigned transactions
        if (!isSigned) {
            String hashCodeTypeHex = HexEncoder.encode(hashCodeType, 4);
            String hashCodeTypeHexLittleEndian = EndianConverter.switchEndianness(hashCodeTypeHex);
            stringBuilder.append(hashCodeTypeHexLittleEndian);
        }

        return stringBuilder.toString();
    }
}
