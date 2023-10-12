package com.qingfeng.module.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @name AESUtils
 * @description 对称加密解密AES算法
 * @author anzi
 * @create 2023/9/8
 **/
public class AESUtils {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "utf-8";
    /**
     * 建议为16位或32位
     */
    private static final String KEY = "jMVCBsFGDQr1USHo";
    /**
     * 必须16位
     * 初始化向量IV不可以为32位，否则异常java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 16 bytes long
     */
    private static final String IV = "jMVCBsFGDQr1USHo";

    /**
     * @name encrypt
     * @description 加密
     * @author anzi
     * @create 2023/9/8 19:08
     **/
    public static String encrypt(String context) {
        try {
            byte[] decode = context.getBytes(CHARSET);
            byte[] bytes = createKeyAndIv(decode, Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @name decrypt
     * @description 解密
     * @author anzi
     * @create 2023/9/8 19:08
     **/
    public static String decrypt(String context) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(context);
            byte[] bytes = createKeyAndIv(decode, Cipher.DECRYPT_MODE);
            return new String(bytes, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @name createKeyAndIv
     * @description 获取key & iv
     * @author anzi
     * @create 2023/9/8 19:08
     **/
    public static byte[] createKeyAndIv(byte[] context, int opmode) throws Exception {
        byte[] key = KEY.getBytes(CHARSET);
        byte[] iv = IV.getBytes(CHARSET);
        return cipherFilter(context, opmode, key, iv);
    }

    /**
     * @name cipherFilter
     * @description 执行操作
     * @author anzi
     * @create 2023/9/8 19:08
     **/
    public static byte[] cipherFilter(byte[] context, int opmode, byte[] key, byte[] iv) throws Exception {
        Key secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(opmode, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(context);
    }


}
