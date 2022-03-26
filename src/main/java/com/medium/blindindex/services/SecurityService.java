package com.medium.blindindex.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Component
public class SecurityService implements ISecurityService {

    @Value("${cms.bidx.hash.key}")
    private String salt;

    @Value("${cms.enc.data.key}")
    private String encKey;

    private static String HASH_ALGO = "SHA-512";
    private static String AES_KEY_GEN_ALGO = "AES";
    private static String AES_CIPHER_ALGO = "AES/CBC/PKCS5Padding";

    public String generateBlindIndex(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
        md.update(Base64.getDecoder().decode(salt));
        byte[] digestBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte digestByte : digestBytes) {
            builder.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
        }
        return builder.substring(builder.length() - 4);
    }

    public String encrypt(String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator generator = KeyGenerator.getInstance(AES_KEY_GEN_ALGO);
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(encKey), AES_KEY_GEN_ALGO);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGO);
        byte[] iv = generator.generateKey().getEncoded();
        String encodedIV = Base64.getEncoder().encodeToString(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String encodedString = Base64.getEncoder().encodeToString(encrypted);
        return encodedString + encodedIV;
    }
    
    public String decrypt(String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String iv = encrypted.substring(encrypted.length() - 24);
        String encryptedString = encrypted.substring(0, encrypted.length() - 24);
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(encKey), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
        return new String(plainText);
    }
}
