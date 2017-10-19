package com.a21vianet.quincysx.library.crypto;


import com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigester;
import com.a21vianet.quincysx.library.crypto.script.Script;
import com.a21vianet.quincysx.library.crypto.utils.Base58;

import static com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigestAlgorithm
        .RIPEMD_160;
import static com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigestAlgorithm.SHA_256;


public class MultiSigBitcoinAddressGenerator {

    private final MessageDigester messageDigester;

    public MultiSigBitcoinAddressGenerator(MessageDigester messageDigester) {
        this.messageDigester = messageDigester;
    }

    public String generateMultiSigAddress(Script redeemScript) {
        return generateMultiSigAddress(DatatypeConverter.printHexBinary(redeemScript.getProgram()));
    }

    public String generateMultiSigAddress(String redeemScriptHex) {
        String firstSha256Hash = messageDigester.digest(redeemScriptHex, SHA_256);
        String ripemd160Hash = messageDigester.digest(firstSha256Hash, RIPEMD_160);
        String ripemd160HashWithVersionByte = "05" + ripemd160Hash;
        String secondSha256Hash = messageDigester.digest(ripemd160HashWithVersionByte, SHA_256);
        String thirdSha256Hash = messageDigester.digest(secondSha256Hash, SHA_256);
        String addressChecksum = thirdSha256Hash.substring(0, 8);
        String binaryBitcoinAddress = ripemd160HashWithVersionByte + addressChecksum;
        return Base58.encode(DatatypeConverter.parseHexBinary(binaryBitcoinAddress));
    }


}
