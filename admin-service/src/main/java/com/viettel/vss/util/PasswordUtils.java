package com.viettel.vss.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PasswordUtils {
    @Value("${hashed.key}")
    private String key;

    public String encode(String password, String salt) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            String saltedPassword = password + salt;
            byte[] hash = sha256Hmac.doFinal(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean verifyPassword(String password, String salt, String hashedPassword) {
        String hashedInput = encode(password, salt);
        return hashedInput.equals(hashedPassword);
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
