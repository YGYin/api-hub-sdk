package com.ygyin.apiplatformsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具类
 */
public class SignatureUtil {

    /**
     * 通过用户参数和密钥生成签名
     *
     * @param body
     * @param secretKey
     * @return
     */
    public static String generateSignature(String body, String secretKey) {
        Digester sha256Digester = new Digester(DigestAlgorithm.SHA256);
        // 将 map 中的用户参数 和 密钥 拼接为新字符串
        String contentStr = body + "." + secretKey;
        // 通过 SHA256 加密字符串作为签名
        return sha256Digester.digestHex(contentStr);
    }
}
