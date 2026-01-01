package com.viettel.vss.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class PasswordUtils {

    public static String encode(String saltedPassword, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static boolean verifyPassword(String saltedPassword, String keyPassword, String hashedPassword) {
        String hashedInput = encode(saltedPassword,keyPassword);
        return hashedInput.equals(hashedPassword);
    }

    public static void main(String[] args) {
        System.out.printf(encode("449daa816099468ccdeb0cb655610e68","111111"));
    }
    public boolean isValidPassword(String password) {
        // Kiểm tra độ dài
        if (password.length() < 8) {
            return false;
        }
        // Kiểm tra số
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        // Kiểm tra chữ cái viết hoa
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // Kiểm tra chữ cái viết thường
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // Kiểm tra ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*()\\-_+=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        return true;
    }

}
