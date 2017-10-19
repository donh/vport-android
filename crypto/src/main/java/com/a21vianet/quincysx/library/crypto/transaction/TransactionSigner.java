package com.a21vianet.quincysx.library.crypto.transaction;


import org.bouncycastle.jce.interfaces.ECPrivateKey;

import java.util.Collections;
import java.util.List;

public class TransactionSigner {

    public Transaction sign(final Transaction transaction, final ECPrivateKey ecPrivateKey) {
        if (transaction.isSigned()) {
            throw new IllegalArgumentException("Transaction is already signed");
        }
        List<TransactionInput> transactionInputs = Collections.emptyList();
        List<TransactionOutput> transactionOutputs = Collections.emptyList();
        return new Transaction(true, transactionInputs, transactionOutputs);
    }

}
