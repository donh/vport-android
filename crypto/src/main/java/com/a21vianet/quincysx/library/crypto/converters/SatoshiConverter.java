package com.a21vianet.quincysx.library.crypto.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class SatoshiConverter {

    private static final MathContext CONTEXT = new MathContext(0, RoundingMode.UNNECESSARY);
    private static final BigDecimal SATOSHI_PER_BTC = new BigDecimal(100_000_000, CONTEXT);
    private static final int SCALE = 8;
    private static final BigDecimal MAX_BTC_VALUE = new BigDecimal(21_000_000);
    private static final BigDecimal MIN_BTC_VALUE = new BigDecimal("0.00000001");
    private static final BigInteger MAX_SATOSHI_VALUE = new BigInteger("2100000000000000");
    private static final BigInteger MIN_SATOSHI_VALUE = new BigInteger("1");

    public static BigInteger btcToSatoshi(final BigDecimal btc) {
        validateInput(btc, MIN_BTC_VALUE, MAX_BTC_VALUE);
        BigDecimal satoshi = btc.multiply(SATOSHI_PER_BTC);
        return satoshi.toBigInteger();
    }

    public static BigDecimal satoshiToBtc(final BigInteger satoshi) {
        validateInput(satoshi, MIN_SATOSHI_VALUE, MAX_SATOSHI_VALUE);
        BigDecimal satoshiBigDecimal = new BigDecimal(satoshi);
        return satoshiBigDecimal.divide(SATOSHI_PER_BTC, SCALE, RoundingMode.UNNECESSARY);
    }

    private static <T extends Number & Comparable<T>> void validateInput(final T value, final T minValue, final T maxValue) {
        int compareToMax = value.compareTo(maxValue);
        int compareToMin = value.compareTo(minValue);
        if (compareToMax > 0 || compareToMin < 0) {
            throw new IllegalArgumentException();
        }
    }
}
