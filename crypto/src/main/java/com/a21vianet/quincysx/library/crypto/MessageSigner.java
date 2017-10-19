package com.a21vianet.quincysx.library.crypto;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class MessageSigner {

    private static final String EC_GEN_PARAM_SPEC = "secp256k1";
    private static final int HEX_RADIX = 16;
    private static final BigInteger MIN_S_VALUE = new BigInteger("1", HEX_RADIX);
    private static final BigInteger MAX_S_VALUE = new BigInteger
            ("7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5D576E7357A4501DDFE92F46681B20A0", HEX_RADIX);
    private static final BigInteger N = new BigInteger
            ("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", HEX_RADIX);
    private final ECDSASigner signer;
    private final ECDomainParameters ecDomainParameters;

    public MessageSigner(DSAKCalculator dsaKCalculator) throws NoSuchProviderException,
            NoSuchAlgorithmException {
        signer = new ECDSASigner(dsaKCalculator);
        ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec(EC_GEN_PARAM_SPEC);
        ecDomainParameters = new ECDomainParameters(params.getCurve(), params.getG(), params.getN
                (), params.getH());
    }

    /**
     * Signs given message with a private key
     *
     * @param messageHex   hex-encoded message to be signed
     * @param ecPrivateKey ECDSA private key
     * @return DER-encoded signature: 0x30 [total-length] 0x02 [R-length] [R] 0x02 [S-length] [S]
     * [sighash-type]
     * @throws IOException
     */
    public String sign(final String messageHex, final ECPrivateKey ecPrivateKey) throws
            IOException {
        byte[] messageBytes = DatatypeConverter.parseHexBinary(messageHex);
        return sign(messageBytes, ecPrivateKey);
    }

    /**
     * Signs given message with a private key
     *
     * @param messageBytes byte-encoded message to be signed
     * @param ecPrivateKey ECDSA private key
     * @return DER-encoded signature: 0x30 [total-length] 0x02 [R-length] [R] 0x02 [S-length] [S]
     * [sighash-type]
     * @throws IOException
     */
    public String sign(final byte[] messageBytes, final ECPrivateKey ecPrivateKey) throws
            IOException {
        BigInteger d = ecPrivateKey.getD();
        CipherParameters cipherParameters = new ECPrivateKeyParameters(d, ecDomainParameters);
        signer.init(true, cipherParameters);
        BigInteger[] bigIntegers = signer.generateSignature(messageBytes);
        BigInteger r = bigIntegers[0];
        BigInteger s = bigIntegers[1];
        BigInteger lowS = getLowValue(s);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DERSequenceGenerator derSequenceGenerator = new DERSequenceGenerator(byteArrayOutputStream);
        ASN1Integer asn1R = new ASN1Integer(r);
        ASN1Integer asn1LowS = new ASN1Integer(lowS);
        derSequenceGenerator.addObject(asn1R);
        derSequenceGenerator.addObject(asn1LowS);
        derSequenceGenerator.close();
        byte[] signatureBytes = byteArrayOutputStream.toByteArray();
        return DatatypeConverter.printHexBinary(signatureBytes);
    }

    /*
    Low S values in signatures (BIP-62):
    The value S in signatures must be between 0x1 and 0x7FFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    5D576E73 57A4501D DFE92F46 681B20A0 (inclusive)
    If S is too high, it must be replaced by S' = 0xFFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE BAAEDCE6
    AF48A03B BFD25E8C D0364141 - S
    */
    private BigInteger getLowValue(BigInteger s) {
        int lowerThanMin = s.compareTo(MIN_S_VALUE);
        int higherThanMax = s.compareTo(MAX_S_VALUE);
        if (lowerThanMin < 0) {
            throw new IllegalArgumentException(String.format("S value must be equal or greater " +
                    "than: %s", MIN_S_VALUE));
        } else if (higherThanMax > 0) {
            return N.subtract(s);
        }
        return s;
    }
}
