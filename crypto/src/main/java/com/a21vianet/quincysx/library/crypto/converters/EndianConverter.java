package com.a21vianet.quincysx.library.crypto.converters;

public class EndianConverter {

    /**
     * Switches between little and big endian byte orders
     *
     * @param hex hex-encoded string
     * @return hex-encoded string with switched byte order
     */
    public static String switchEndianness(final String hex) {
        int lengthInBytes = hex.length() / 2;
        char[] chars = new char[hex.length()];
        for (int index = 0; index < lengthInBytes; index++) {
            int reversedIndex = lengthInBytes - 1 - index;
            chars[reversedIndex * 2] = hex.charAt(index * 2);
            chars[reversedIndex * 2 + 1] = hex.charAt(index * 2 + 1);
        }
        return new String(chars);
    }
}
