package com.allinpay.checkout.utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

public class RSAutil {

    public static String generateSign(Map<String, String> params, String priKey) throws Exception {
        String signString = spliceParams(params);
        System.out.println("待签名的字符串: " + signString);

        // 生成签名
        return rsaSign(signString, priKey);
    }

    //生成私钥加密后的字符串
    private static String rsaSign(String data, String priKey) throws Exception {
        // 解码Base64格式的私钥
        byte[] keyBytes = java.util.Base64.getDecoder().decode(priKey);

        // 生成私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // 签名
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();

        // Base64编码签名结果
        return Base64.getEncoder().encodeToString(signBytes);
    }

    public static boolean verifySign(Map<String, String> params, String Key) throws Exception {
        // 移除sign字段并过滤掉空值
        String signString = spliceParams(params);
        System.out.println("待验证签名的字符串: " + signString);

        // 验证签名
        return rsaVerify(signString, Key, params.get("sign"));
    }

    private static boolean rsaVerify(String data, String Key, String signatureStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解码Base64格式的密钥
        byte[] keyBytes = Base64.getDecoder().decode(Key);

        // 生成密钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // 解码Base64格式的签名
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);

        // 验证签名
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);
    }

    //拼接参数
    private static String spliceParams(Map<String, String> params) {
        // 移除sign字段并过滤掉空值
        Map<String, String> filteredParams = new TreeMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!"sign".equals(entry.getKey()) && entry.getValue() != null && !entry.getValue().isEmpty()) {
                filteredParams.put(entry.getKey(), entry.getValue());
            }
        }

        // 拼接成URL键值对格式的字符串
        StringBuilder stringToSign = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : filteredParams.entrySet()) {
            if (!isFirst) {
                stringToSign.append("&");
            }
            stringToSign.append(entry.getKey()).append("=").append(entry.getValue());
            isFirst = false;
        }

        String signString = stringToSign.toString();
        return signString;
    }
}