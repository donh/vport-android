package com.a21vianet.quincysx.library.crypto.messagedigesters;


import com.a21vianet.quincysx.library.crypto.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageDigester {

    private final Map<MessageDigestAlgorithm, MessageDigest> messageDigesters;
    private final Set<MessageDigestAlgorithm> algorithms;

    /**
     * Initializes the message digester using a given security provider and a set of supported
     * message digest algorithms
     *
     * @param algorithms set of algorithms which should be supported by the digester.
     *                   They must be supported by the security provider(s)
     * @param provider   security provider implementation
     * @throws NoSuchAlgorithmException
     */
    public MessageDigester(Set<MessageDigestAlgorithm> algorithms, Provider provider) throws
            NoSuchAlgorithmException {
        //Android Jdk 问题 此方法无效
        //Security.addProvider(provider);
        this.algorithms = algorithms;
        messageDigesters = new HashMap<>(algorithms.size());
        initMessageDigesters(provider);
    }

    private void initMessageDigesters(Provider provider) throws NoSuchAlgorithmException {
        for (MessageDigestAlgorithm algorithm : algorithms) {
            messageDigesters.put(algorithm, MessageDigest.getInstance(algorithm.toString(),
                    provider));
        }
    }

    /**
     * @param text      string to be hashed
     * @param algorithm a name of a hash function which will by applied on the input text
     * @return hashed text
     */
    public String digest(String text, MessageDigestAlgorithm algorithm) {
        MessageDigest messageDigest = messageDigesters.get(algorithm);
        if (messageDigest == null) {
            throw new IllegalArgumentException(String.format("%s algorithm is not supported",
                    algorithm));
        }
        byte[] inputBytes = DatatypeConverter.parseHexBinary(text);
        byte[] hashBytes = messageDigest.digest(inputBytes);
        return DatatypeConverter.printHexBinary(hashBytes);
    }
}
