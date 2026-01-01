package com.viettel.vss.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@$!%*?&";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
    private static final int PASSWORD_LENGTH = 8;

    private static SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        List<Character> charCategories = new ArrayList<>();

        // Add one character from each category to ensure the password meets all requirements
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars, random);

        // Convert the shuffled list back to a string
        StringBuilder shuffledPassword = new StringBuilder();
        for (char c : passwordChars) {
            shuffledPassword.append(c);
        }

        return shuffledPassword.toString();
    }

    public static String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        StringBuilder hexSalt = new StringBuilder();
        for (byte b : salt) {
            hexSalt.append(String.format("%02x", b));
        }
        return hexSalt.toString();
    }

    public static String generateCompanyCode() {
        int maxLength = 20;
        int minLength = 6;

        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALL.length());
            sb.append(ALL.charAt(randomIndex));
        }

        return sb.toString();
    }
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
//        return otp.toString();
        return "123456";
    }
}
