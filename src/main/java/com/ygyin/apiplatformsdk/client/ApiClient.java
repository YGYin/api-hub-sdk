package com.ygyin.apiplatformsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ygyin.apiplatformsdk.model.SampleModel;


import java.util.HashMap;
import java.util.Map;

import static com.ygyin.apiplatformsdk.utils.SignatureUtil.generateSignature;


/**
 * 用于调用第三方接口的客户端
 */
public class ApiClient {

    private String accessKey;

    private String secretKey;

    private static final String GATEWAY_ADDRESS = "http://localhost:8080";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getInfoByGet(String info) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("info", info);
        String res = HttpUtil.get(GATEWAY_ADDRESS + "/api/sample/get", paramMap);
        System.out.println(res);
        return res;
    }

    public String getInfoByPost(String info) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("info", info);
        String res = HttpUtil.post(GATEWAY_ADDRESS + "/api/sample/post", paramMap);
        System.out.println(res);
        return res;
    }

    /**
     * @param paramBody 用户传递的参数
     * @return
     */
    private Map<String, String> getKeyIntoHeaderMap(String paramBody) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        // 密钥不能直接放到请求头中发送
//        map.put("secretKey", secretKey);
        map.put("randomNum", RandomUtil.randomNumbers(4));
        map.put("paramBody", paramBody);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 用户参数 + 密钥 --签名生成算法-> 不可解谜的值
        map.put("signature", generateSignature(paramBody, secretKey));

        return map;
    }

    public String getSampleInfoByPost(SampleModel sampleModel) {
        // 将对象转换为 json 字符串
        String json = JSONUtil.toJsonStr(sampleModel);
//        String res = HttpRequest.post("http://localhost:8234/api/test/sample/")
//                      .body(json)
//                      .execute().body();
        // 对应 restful 接口，将 ak sk 加入到请求头中
        HttpResponse httpResp = HttpRequest.post(GATEWAY_ADDRESS + "/api/sample/info")
                .addHeaders(getKeyIntoHeaderMap(json))
                .body(json)
                .execute();
        System.out.println("HTTP Response Status: " + httpResp.getStatus());

        String res = httpResp.body();
        System.out.println(res);
        return res;
    }
}
