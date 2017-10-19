package com.a21vianet.quincysx.library.crypto;

import com.a21vianet.quincysx.library.crypto.crypto.hd.HDDerivationException;
import com.a21vianet.quincysx.library.crypto.crypto.hd.HDUtils;
import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicCode;
import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicException;

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.macs.HMac;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.math.ec.FixedPointUtil;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static com.a21vianet.quincysx.library.crypto.guava.Preconditions.checkArgument;
import static com.a21vianet.quincysx.library.crypto.guava.Preconditions.checkState;


/**
 * Created by wang.rongqiang on 2017/5/17.
 */

public class DHCreate {
    public static final HMac MASTER_HMAC_SHA512 = HDUtils.createHmacSha512Digest("Bitcoin seed"
            .getBytes());
//    /**
//     * 加密存储的密钥种子
//     */
//    private EncryptedData mEncryptedMnemonicSeed;

    /**
     * 产生的密钥
     */
    private BigInteger mPriv;

    /**
     * 产生私钥的种子
     */
    private byte[] mMnemonicSeed;

    /**
     * The parameters of the secp256k1 curve that Bitcoin uses.
     */
    public static final ECDomainParameters CURVE;
    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");

    /**
     * Equal to CURVE.getN().shiftRight(1), used for canonicalising the S value of a signature.
     * If you aren't
     * sure what this is about, you can ignore it.
     */
    public static final BigInteger HALF_CURVE_ORDER;

    public DHCreate(MnemonicCode mnemonicCode)
            throws MnemonicException
            .MnemonicLengthException {
        this(mnemonicCode, new SecureRandom());
    }

    public DHCreate(MnemonicCode mnemonicCode, SecureRandom random)
            throws MnemonicException
            .MnemonicLengthException {
        this(mnemonicCode, randomByteFromSecureRandom(random, 16));
    }

    public DHCreate(MnemonicCode mnemonicCode, byte[] mnemonicSeed) throws
            MnemonicException.MnemonicLengthException {
        this.mMnemonicSeed = mnemonicSeed;
        byte[] hdSeed = seedFromMnemonic(mnemonicSeed, mnemonicCode);
        //此处能够获得密钥
        mPriv = createMasterPrivateKeyint(hdSeed);
        Arrays.fill(hdSeed, (byte) 0);
    }

    /**
     * 获得私钥
     *
     * @return
     */
    public BigInteger getPrivKey() {
        return mPriv;
    }

    /**
     * 获得私钥
     *
     * @return
     */
//    public String getPrivKeyString() {
//        try {
//            return generateBitcoinPrivateKey(generateRawBitcoinPrivateKey(mPriv));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    /**
     * 获得助记词
     *
     * @return
     * @throws MnemonicException.MnemonicLengthException
     */
    public List<String> getWords() throws MnemonicException.MnemonicLengthException {
//        //解密算法
//        byte[] decryptSeed = new EncryptedData(mEncryptedMnemonicSeed
//                .toEncryptedString()).decrypt(password);
        return MnemonicCode.instance().toMnemonic(mMnemonicSeed);
    }

    /**
     * 通过助记词回复私钥
     *
     * @param list 助记词
     * @return
     */
    public static BigInteger resetPrivKey(List list) {
        byte[] bytes = MnemonicCode.toSeed(list, "");
        BigInteger masterPrivateKeyint = createMasterPrivateKeyint(bytes);
        Arrays.fill(bytes, (byte) 0);
        return masterPrivateKeyint;
    }

//    /**
//     * 通过助记词回复私钥
//     *
//     * @param list 助记词
//     * @return
//     */
//    public static String resetPrivKeyString(List list) {
//        try {
//            return generateBitcoinPrivateKey(generateRawBitcoinPrivateKey(resetPrivKey(list)));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    private static byte[] randomByteFromSecureRandom(SecureRandom random, int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static final byte[] seedFromMnemonic(byte[] mnemonicSeed, MnemonicCode...
            mnemonicCode) throws MnemonicException
            .MnemonicLengthException {
        MnemonicCode mnemonic = mnemonicCode.length > 0 ? mnemonicCode[0] : MnemonicCode.instance();
        return mnemonic.toSeed(mnemonic.toMnemonic(mnemonicSeed), "");
    }

//    public static String generateRawBitcoinPrivateKey(BigInteger ecPrivateKey) {
//        return String.format("%064x", ecPrivateKey);
//    }
//
//    public static String generateBitcoinPrivateKey(String rawBitcoinPrivateKey)
//            throws NoSuchAlgorithmException {
//        Provider provider = new BouncyCastleProvider();
//        Set<MessageDigestAlgorithm> messageDigestAlgorithms = new HashSet<>(Arrays.asList
//                (MessageDigestAlgorithm.values()));
//
//        MessageDigester messageDigester = new MessageDigester(messageDigestAlgorithms,
//                provider);
//
//        String prefix = "80";
//        String rawBitcoinPrivateKeyWithVersionByte = prefix + rawBitcoinPrivateKey + "01";
//        String firstSha256Hash = messageDigester.digest(rawBitcoinPrivateKeyWithVersionByte,
//                SHA_256);
//        String secondSha256Hash = messageDigester.digest(firstSha256Hash, SHA_256);
//        String privateKeyCheckSum = secondSha256Hash.substring(0, 8);
//        String binaryPrivateKey = rawBitcoinPrivateKeyWithVersionByte + privateKeyCheckSum;
//        return Base58.encode(parseHexBinary(binaryPrivateKey));
//    }

    /**
     * 生成私钥
     *
     * @param seed
     * @return
     * @throws HDDerivationException
     */
    public static BigInteger createMasterPrivateKeyint(byte[] seed) throws
            HDDerivationException {
        checkArgument(seed.length > 8, "Seed is too short and could be brute forced");
        // Calculate I = HMAC-SHA512(key="Bitcoin seed", msg=S)
        byte[] i = HDUtils.hmacSha512(MASTER_HMAC_SHA512, seed);
        // Split I into two 32-byte sequences, Il and Ir.
        // Use Il as master secret key, and Ir as master chain code.
        checkState(i.length == 64, i.length);
        byte[] il = Arrays.copyOfRange(i, 0, 32);
//        byte[] ir = Arrays.copyOfRange(i, 32, 64);
        Arrays.fill(i, (byte) 0);
        BigInteger priv = new BigInteger(1, il);
        Arrays.fill(il, (byte) 0);
        assertNonZero(priv, "Generated master key is invalid.");
        assertLessThanN(priv, "Generated master key is invalid.");
        return priv;
    }

    private static void assertNonZero(BigInteger integer, String errorMessage) {
        if (integer.equals(BigInteger.ZERO))
            throw new HDDerivationException(errorMessage);
    }

    static {
        // Tell Bouncy Castle to precompute data that's needed during secp256k1 calculations.
        // Increasing the width
        // number makes calculations faster, but at a cost of extra memory usage and with
        // decreasing returns. 12 was
        // picked after consulting with the BC team.
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS
                .getN(),
                CURVE_PARAMS.getH());
        HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);

    }

    private static void assertLessThanN(BigInteger integer, String errorMessage) {
        if (integer.compareTo(CURVE.getN()) > 0)
            throw new HDDerivationException(errorMessage);
    }
}
