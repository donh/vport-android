package com.a21vianet.quincysx.library.crypto.transaction;

import java.math.BigDecimal;

public class TransactionOutput {

    private final BigDecimal value;
    private final int pkScriptLength;
    private final String pkScript;

    public TransactionOutput(BigDecimal value, String pkScript) {
        this(value, pkScript.length(), pkScript);
    }

    public TransactionOutput(BigDecimal value, int pkScriptLength, String pkScript) {
        this.value = value;
        this.pkScriptLength = pkScriptLength;
        this.pkScript = pkScript;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getPkScriptLength() {
        return pkScriptLength;
    }

    public String getPkScript() {
        return pkScript;
    }
}
