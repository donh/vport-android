package com.a21vianet.quincysx.library.crypto;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.PublicKey;

public class SignatureValidator {

    private static final String EC_GEN_PARAM_SPEC = "secp256k1";
    private final ECDSASigner signer;
    private final ECDomainParameters ecDomainParameters;

    public SignatureValidator() {
        signer = new ECDSASigner();
        ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec(EC_GEN_PARAM_SPEC);
        ecDomainParameters = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    }

    // todo: overload validate - (String message, String signature, PublicKey publicKey)
    public boolean validate(String message, BigInteger r, BigInteger s, PublicKey publicKey) {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        ECPoint q = ecPublicKey.getQ();
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(q, ecDomainParameters);
        signer.init(false, ecPublicKeyParameters);
        byte[] messageBytes = DatatypeConverter.parseHexBinary(message);
        return signer.verifySignature(messageBytes, r, s);
    }
}
