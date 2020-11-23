package com.zss.base.util;

import com.alibaba.fastjson.JSON;
import com.zss.base.config.HttpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 15:14
 * @desc Http工具类
 * 目前就先写这两个
 */
@Slf4j
@SuppressWarnings("unused")
public class HttpUtil {

    private static final int STEP = 2;

    /**
     * doGet
     */
    public static CloseableHttpResponse doGet(String uri, String... params) {
        CloseableHttpClient httpClient = HttpClientPool.getHttpClient();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i += STEP) {
            stringBuilder.append(params[i])
                    .append("=")
                    .append(params[i + 1])
                    .append("&");
        }
        uri = uri + "?" + stringBuilder;

        HttpGet httpGet = new HttpGet(uri);
        HttpClientContext context = HttpClientContext.create();

        log.info("Executing Request [{}]", httpGet.getURI());
        try {
            return httpClient.execute(httpGet, context);
        } catch (IOException e) {
            log.error("发送Get发生未知异常: [{}]", e.getMessage());
            return null;
        }
    }

    /**
     * doPost
     *
     * @param uri         uri
     * @param requestBody 参数
     * @return response
     */
    public static CloseableHttpResponse doPost(String uri, Object requestBody) {
        CloseableHttpClient httpClient = HttpClientPool.getHttpClient();

        HttpPost httpPost = new HttpPost(uri);
        String entity = JSON.toJSONString(requestBody);
        try {
            StringEntity stringEntity = new StringEntity(entity);
            stringEntity.setContentType("text/json");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            httpPost.setEntity(stringEntity);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            //执行上下文可以在本地自定义。
            HttpClientContext context = HttpClientContext.create();
            //设置本地上下文级别的上下文属性

            log.info("Executing Request [{}]", httpPost.getURI());
            try {
                return httpClient.execute(httpPost, context);
            } catch (IOException e) {
                log.error("获取Response的时候发生异常: [{}]", e.getMessage());
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            log.error("发送Post时发生异常: [{}]", e.getMessage());
            return null;
        }
    }
}
