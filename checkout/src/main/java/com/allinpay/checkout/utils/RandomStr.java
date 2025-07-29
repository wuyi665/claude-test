package com.allinpay.checkout.utils;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStr {

    // 字符集合
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUM = UPPER + LOWER + DIGITS;
    private static final String ALPHANUM_SPECIAL = ALPHANUM + "!@#$%^&*()_+-=[]{}|;:,.<>?";

    // 安全随机数生成器
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomStr() {
        // 工具类，防止实例化
    }

    /**
     * 生成包含大小写字母和数字的32位随机字符串（安全版本）
     * @return 32位随机字符串
     */
    public static String generateAlphanumericSecure() {
        return generateSecure(32, ALPHANUM.toCharArray());
    }

    /**
     * 生成包含大小写字母、数字和特殊字符的32位随机字符串（安全版本）
     * @return 32位随机字符串
     */
    public static String generateComplexSecure() {
        return generateSecure(32, ALPHANUM_SPECIAL.toCharArray());
    }

    /**
     * 使用Base64编码生成URL安全的随机字符串
     * @return 32位Base64随机字符串
     */
    public static String generateBase64Secure() {
        // 生成24字节随机数据 -> Base64编码后为32字符
        byte[] randomBytes = new byte[24];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * 生成HEX格式的随机字符串（32字节 = 64字符）
     * @return 64位HEX随机字符串
     */
    public static String generateHexSecure() {
        // 32字节随机数据
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        StringBuilder sb = new StringBuilder(64);
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 使用高性能ThreadLocalRandom生成简单随机字符串
     * @return 32位随机字母数字字符串
     */
    public static String generateSimple() {
        return generate(32, ALPHANUM.toCharArray(), ThreadLocalRandom.current());
    }

    /**
     * 基于字符集生成安全的随机字符串
     * @param length 字符串长度
     * @param symbols 可用字符数组
     * @return 随机字符串
     */
    private static String generateSecure(int length, char[] symbols) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(symbols[SECURE_RANDOM.nextInt(symbols.length)]);
        }
        return sb.toString();
    }

    /**
     * 基于字符集使用指定随机数生成器生成随机字符串
     * @param length 字符串长度
     * @param symbols 可用字符数组
     * @param random 随机数生成器
     * @return 随机字符串
     */
    private static String generate(int length, char[] symbols, Random random) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(symbols[random.nextInt(symbols.length)]);
        }
        return sb.toString();
    }
}
