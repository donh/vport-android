package com.a21vianet.quincysx.library.crypto.encoders;

import java.math.BigInteger;
import java.util.function.Function;

public class HexEncoder {

    private static final int HEX_RADIX = 16;
    private static final int HEX_BYTE_LENGTH = 2;

    public static String encode(final int integer) {
        return Integer.toHexString(integer);
    }

    public static String encode(final int integer, final int lengthInBytes) {
        return String.format("%0" + lengthInBytes * HEX_BYTE_LENGTH + "x", integer);
    }

    public static String encode(final BigInteger bigInteger) {
        return bigInteger.toString(HEX_RADIX);
    }

    public static String encode(final BigInteger bigInteger, final int lengthInBytes) {
        return String.format("%0" + lengthInBytes * HEX_BYTE_LENGTH + "x", bigInteger);
    }

}
