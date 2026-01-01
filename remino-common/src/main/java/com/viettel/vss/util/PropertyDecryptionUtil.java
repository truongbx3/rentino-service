package com.viettel.vss.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PropertyDecryptionUtil {
    
    private static final String ALGORITHM = "AES";
    
    private static final String SECRET_KEY = "secret_key";
    
    private static final String SALT = "secret_salt";
    
    /* Encrypt algorithm
    public static String encrypt(String input, String secretKey, String salt) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromSecretKeyAndSalt(secretKey, salt));
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }
    */
    
    public static String decrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromSecretKeyAndSalt(SECRET_KEY, SALT));
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(plainText);
    }
    
    private static SecretKey getKeyFromSecretKeyAndSalt(String secretKey, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM);
    }
    
}
