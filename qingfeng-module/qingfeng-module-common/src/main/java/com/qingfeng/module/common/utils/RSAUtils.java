package com.qingfeng.module.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  rsa 加密工具
 */
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        System.out.println(publicKeyString);
        //得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
        System.out.println(privateKeyString);
        keyMap.put(PUBLIC_KEY, publicKeyString);
        keyMap.put(PRIVATE_KEY, privateKeyString);
        return keyMap;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes =decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encode(key.getEncoded());
    }

    /**
     * base64 加密
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encodeBase64(bytes));
    }
    /**
     * base64 解密
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decodeBase64(base64.getBytes());
    }

    /**
     * @Description: 解密
     * @Param: [data, privateKey]
     * @return: java.lang.String
     * @Author: anxingtao
     * @Date: 2019-3-13 11:06
     */
    public static String decrypt(String data, String privateKey) throws Exception {
        byte[] pubdata = decryptByPrivateKey(decode(data), privateKey);
        return new String(pubdata, "UTF-8");
    }

    /**
     * @Description: 加密
     * @Param: [data, publicKey]
     * @return: java.lang.String
     * @Author: anxingtao
     * @Date: 2019-3-13 11:10
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        byte[] pubdata = encryptByPublicKey(data.getBytes("UTF-8"), publicKey);
        String outString = new String(encode(pubdata));
        return outString;
    }

    /**
     * @title encrypriva
     * @description 私钥加密
     * @author Administrator
     * @updateTime 2021/8/11 16:31
     */
    public static String encrypk(String data, String privateKey) throws Exception {
        byte[] resdata = encryptByPrivateKey(data.getBytes("UTF-8"), privateKey);
        String outString = new String(encode(resdata));
        return outString;
    }

    /**
     * @title decrypk
     * @description 公钥解密
     * @author Administrator
     * @updateTime 2021/8/11 16:32
     */
    public static String decrypk(String data, String publicKey) throws Exception {
        byte[] resdata = decryptByPublicKey(decode(data), publicKey);
        return new String(resdata, "UTF-8");
    }



//    public static void main(String[] args) throws Exception {
//        Map map = genKeyPair();
//        String publicKey = map.get(PUBLIC_KEY).toString();
//        String privateKey = map.get(PRIVATE_KEY).toString();
//        System.out.println("publicKey = " + publicKey);
//        System.out.println("privateKey = " + privateKey);
//
////        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIJL3tncqX9iUOe59KWhlktI36+zV78V6yQ8D/figggDmrm205hGD7tQbwwvELJvblZhrhiWYzqi9pXsrwDZAOOd9ueBHEuWP9hVdn627yJOpTXiont6QjbppTZTOGz/qM7dF87F3obtE//TlfRCn7kJqeAtQVI7PkZs96OR/39BAgMBAAECgYAEyRKTP9Z91jeQezTwDGI/u5pzkourefub5hTUDt6lurQpE/zrf88+GvMpKGVdmFgeFhJV1tgoJCoYUYwP5OhyZkVBXDxerAD5W5bRH9P65dFEHHOj7Sjh/3p1ANrmtlE2oo4pgjM9yYekkXbff7DB9YAUNDbc2RLj1g0Shyzi3QJBALh2+8XDi5QCMst7jCuhha4Pg9Tp3sjr1nFCXjQcr+yLFOR5l/yp3l9caNVc1rhOiai9nL17nld5jBsBqmtK6VcCQQC00zy5JFN8RPJf8201/oMEVGZ20+fNORyuys9Cv3eZxXSmvsCV3LP1Qzm1AMAZchYlCkmS+BvpwJbo4+/6asUnAkB8Y3eZvygDEYxxHInD7jJ30myCiYY6lJXRYACoQWP8LTlUbA4qSd3zDhx/LBB6zjMTv3DPcWmv8P2iRHhHy4cXAkAax7YUjWa33tYuk7S27Gym9YD/VD7eS/kNggfUENs7sNyn3VRUXY1hY4VmYygQHK9e6PkghRJNOjKfZzKhWbK9AkEApBT6RgD0LUIa2jYfUNiCA1MbjwzRo+Tjm+7XcP7MzUDW+xtDxs/hidzHIlc6xbcLIkvQLPfUJp4xXtp93jl6UQ==";
////        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCS97Z3Kl/YlDnufSloZZLSN+vs1e/FeskPA/34oIIA5q5ttOYRg+7UG8MLxCyb25WYa4YlmM6ovaV7K8A2QDjnfbngRxLlj/YVXZ+tu8iTqU14qJ7ekI26aU2Uzhs/6jO3RfOxd6G7RP/05X0Qp+5CangLUFSOz5GbPejkf9/QQIDAQAB";
//        String msg = "{\"dev_id\":\"868989034328943\",\"user_id\":\"\",\"team_id\":\"\"}";
//        String sm = RSAUtils.encrypt(msg,publicKey);
//        System.out.println(sm);
//        String s = RSAUtils.decrypt(sm,privateKey);
//        System.out.println(s);
//
//    }


}