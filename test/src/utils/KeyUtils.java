/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 *
 * @author datth
 */
public class KeyUtils {

    public static PublicKey hexToPublicKey(String hexString) throws Exception {
        // Chuyển Hex thành mảng byte
        byte[] keyBytes = StringUtils.hexStringToByteArray(hexString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); 
        return keyFactory.generatePublic(keySpec);
    }
    public static boolean verifySignature(byte[] publicKeyBytes, byte[] dataToVerify, byte[] signedData) throws Exception {
        short expLen = ByteBuffer.wrap(new byte[]{publicKeyBytes[publicKeyBytes.length - 2], publicKeyBytes[publicKeyBytes.length - 1]}).getShort();
        short modLen = ByteBuffer.wrap(new byte[]{publicKeyBytes[publicKeyBytes.length - 4], publicKeyBytes[publicKeyBytes.length - 3]}).getShort();
        byte[] modulusBytes = Arrays.copyOfRange(publicKeyBytes, 0, modLen);
        byte[] exponentBytes = Arrays.copyOfRange(publicKeyBytes, modLen, modLen + expLen);
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
        Signature rsaSign = Signature.getInstance("MD5withRSA");
        rsaSign.initVerify(publicKey);
        rsaSign.update(dataToVerify);
        return rsaSign.verify(signedData);
    }

}
