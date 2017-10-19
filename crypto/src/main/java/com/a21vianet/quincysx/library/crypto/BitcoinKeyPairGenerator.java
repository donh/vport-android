package com.a21vianet.quincysx.library.crypto;

import com.a21vianet.quincysx.library.crypto.crypto.exception.AddressFormatException;
import com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigester;
import com.a21vianet.quincysx.library.crypto.utils.Base58;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static com.a21vianet.quincysx.library.crypto.DatatypeConverter.parseHexBinary;
import static com.a21vianet.quincysx.library.crypto.DatatypeConverter.printHexBinary;
import static com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigestAlgorithm
        .RIPEMD_160;
import static com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigestAlgorithm.SHA_256;


public class BitcoinKeyPairGenerator {

    private static final String EC_GEN_PARAM_SPEC = "secp256k1";
    private static final String KEY_PAIR_GEN_ALGORITHM = "ECDSA";
    private static final String MAIN_NET_PRIVATE_KEY_PREFIX = "80";
    private static final String MAIN_NET_PRIVATE_KEY_SUFFIX = "01";
    private static final String MAIN_NET_PUBLIC_KEY_PREFIX = "00";
    private final ECParameterSpec ecParameterSpec;
    private final KeyPairGenerator keyPairGenerator;
    private final KeyFactory keyFactory;
    private final MessageDigester messageDigester;

    /**
     * Initializes the Bitcoin keys generator using a given secure random, security provider and
     * message digester
     *
     * @param secureRandom
     * @param provider
     * @param messageDigester
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     */
    public BitcoinKeyPairGenerator(SecureRandom secureRandom, Provider provider, MessageDigester
            messageDigester)
            throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        //Android Jdk 问题 此方法无效
        //Security.addProvider(provider);
        ecParameterSpec = ECNamedCurveTable.getParameterSpec(EC_GEN_PARAM_SPEC);
        keyPairGenerator = KeyPairGenerator.getInstance(KEY_PAIR_GEN_ALGORITHM, provider);
        keyPairGenerator.initialize(ecParameterSpec, secureRandom); //TODO: external seed
        keyFactory = KeyFactory.getInstance(KEY_PAIR_GEN_ALGORITHM, provider);
        this.messageDigester = messageDigester;
    }

    /**
     * Generates random mainNet Bitcoin key pair
     *
     * @return mainNet Bitcoin key pair
     */
    public BitcoinKeyPair generateBitcoinKeyPair() {
        KeyPair keyPair = generateEcdsaKeyPair();
        return generateBitcoinKeyPair(keyPair);
    }

    public BitcoinKeyPair generateBitcoinKeyPair(KeyPair keyPair) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        //生成出普通 raw 私钥
        String rawBitcoinPrivateKey = generateRawBitcoinPrivateKey(ecPrivateKey);

        String rawBitcoinPublicKey = generateRawBitcoinPublicKey(ecPublicKey, false);

        //针对 区块链生成 WIF 格式私钥
        String bitcoinPrivateKey = generateBitcoinPrivateKey(rawBitcoinPrivateKey, false);

        return new BitcoinKeyPair(bitcoinPrivateKey, rawBitcoinPublicKey);
    }

    /**
     * Generates a random ECDSA key pair
     *
     * @return ECDSA (secp256k1) key pair
     */
    public KeyPair generateEcdsaKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Generates a ECDSA (secp256k1) keypair from a secret exponent
     *
     * @param secretExponent
     * @return ECDSA key pair
     * @throws InvalidKeySpecException
     */
    public KeyPair generateEcdsaKeyPair(BigInteger secretExponent) throws InvalidKeySpecException {
        KeySpec privateKeySpec = new ECPrivateKeySpec(secretExponent, ecParameterSpec);
        ECPoint ecPoint = ecParameterSpec.getG().multiply(secretExponent);
        KeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * Transforms ECDSA private key into the raw Bitcoin private key
     *
     * @param ecPrivateKey ECDSA private key
     * @return raw Bitcoin private key (secret exponent hex)
     */
    public String generateRawBitcoinPrivateKey(ECPrivateKey ecPrivateKey) {
        return String.format("%064x", ecPrivateKey.getD());
    }

    /**
     * Transforms a raw Bitcoin private key into the mainNet or testNet Bitcoin private key
     *
     * @param rawBitcoinPrivateKey private key secret exponent hex
     * @param iscompress           是否要针对私钥进行压缩
     * @return mainNet or testNet Bitcoin private key in a Wallet Import Format (WIF)
     */
    public String generateBitcoinPrivateKey(String rawBitcoinPrivateKey, boolean iscompress) {
        String rawBitcoinPrivateKeyWithVersionByte = MAIN_NET_PRIVATE_KEY_PREFIX +
                rawBitcoinPrivateKey;
        if (iscompress) {
            rawBitcoinPrivateKeyWithVersionByte = rawBitcoinPrivateKeyWithVersionByte +
                    MAIN_NET_PRIVATE_KEY_SUFFIX;
        }

        String firstSha256Hash = messageDigester.digest(rawBitcoinPrivateKeyWithVersionByte,
                SHA_256);
        String secondSha256Hash = messageDigester.digest(firstSha256Hash, SHA_256);
        String privateKeyCheckSum = secondSha256Hash.substring(0, 8);
        String binaryPrivateKey = rawBitcoinPrivateKeyWithVersionByte + privateKeyCheckSum;
        return Base58.encode(parseHexBinary(binaryPrivateKey));
    }


    /**
     * Transforms ECDSA public key into the raw Bitcoin address
     *
     * @param ecPublicKey ECDSA public key
     * @param iscompress  是否压缩地址
     * @return Raw Bitcoin address: 65 bytes, 1 byte 0x04, 32 bytes corresponding to X
     * coordinate, 32 bytes
     * corresponding to Y coordinate
     */
    public String generateRawBitcoinPublicKey(ECPublicKey ecPublicKey, boolean iscompress) {
        if (iscompress) {
            //生成压缩 公钥
            ECPoint q = ecPublicKey.getQ();
            BigInteger affineXCoord = q.getAffineXCoord().toBigInteger();
            BigInteger affineYCoord = q.getAffineYCoord().toBigInteger();
            BigInteger remainder = affineYCoord.remainder(new BigInteger("2"));

            String prefix;
            if (remainder.intValue() == 0) {
                prefix = "02";
            } else {
                prefix = "03";
            }
            return prefix + String.format("%064x", affineXCoord);
        } else {
            //生成未压缩 公钥
            ECPoint q = ecPublicKey.getQ();
            BigInteger affineXCoord = q.getAffineXCoord().toBigInteger();
            BigInteger affineYCoord = q.getAffineYCoord().toBigInteger();
            return "04" + String.format("%064x", affineXCoord) + String.format("%064x",
                    affineYCoord);
        }
    }

    /**
     * Transforms a raw Bitcoin public key into the Bitcoin address
     *
     * @param rawBitcoinPublicKey 65 bytes, 1 byte 0x04, 32 bytes corresponding to X coordinate,
     *                            32 bytes
     *                            corresponding to Y coordinate
     * @return mainNet or testNet Bitcoin address in a Wallet Import Format (WIF)
     */
    public String generateBitcoinAddress(String rawBitcoinPublicKey) {
        String firstSha256Hash = messageDigester.digest(rawBitcoinPublicKey, SHA_256);
        String ripemd160Hash = messageDigester.digest(firstSha256Hash, RIPEMD_160);
        String prefix = MAIN_NET_PUBLIC_KEY_PREFIX;
        String ripemd160HashWithVersionByte = prefix + ripemd160Hash;
        String secondSha256Hash = messageDigester.digest(ripemd160HashWithVersionByte, SHA_256);
        String thirdSha256Hash = messageDigester.digest(secondSha256Hash, SHA_256);
        String addressChecksum = thirdSha256Hash.substring(0, 8);
        String binaryBitcoinAddress = ripemd160HashWithVersionByte + addressChecksum;
        return Base58.encode(parseHexBinary(binaryBitcoinAddress));
    }

    /**
     * Gets the RIPEMD-160 public key hash from the Bitcoin address
     *
     * @param bitcoinAddress testNet or mainNet Bitcoin address
     * @return RIPEMD-160 hash of a Bitcoin public key without version byte and checksum
     */
    public static String getRipmed160Hash(String bitcoinAddress) {
        byte[] decodedAddressBytes = new byte[0];
        try {
            decodedAddressBytes = Base58.decode(bitcoinAddress);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        String decodedAddressHex = printHexBinary(decodedAddressBytes);
        return decodedAddressHex.substring(2, decodedAddressHex.length() - 8);
    }

    /**
     * Gets a raw Bitcoin private key from the Wallet Import Format (WIF) private key
     *
     * @param bitcoinPrivateKey testNet or mainNet Bitcoin private key in a Wallet Import Format
     *                          (WIF)
     * @return raw Bitcoin private key
     */
    public static String getRawBitcoinPrivateKey(String bitcoinPrivateKey, boolean iscompress) {
        byte[] decodedPrivateKey = new byte[0];
        try {
            decodedPrivateKey = Base58.decode(bitcoinPrivateKey);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        String rawPrivateKey = printHexBinary(decodedPrivateKey);
        if (iscompress) {
            return rawPrivateKey.substring(2, rawPrivateKey.length() - 10);
        } else {
            return rawPrivateKey.substring(2, rawPrivateKey.length() - 8);
        }
    }

}
