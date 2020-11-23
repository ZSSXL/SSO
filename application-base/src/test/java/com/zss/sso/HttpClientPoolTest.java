package com.zss.sso;

import com.alibaba.fastjson.JSON;
import com.zss.base.config.HttpClientPool;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 15:00
 * @desc HttpClient连接池测试
 */
public class HttpClientPoolTest {

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientPool.getHttpClient();

        HttpPost httpPost = new HttpPost("http://127.0.0.1:8881/one");

        String someThing = "I am post String";
        String requestBody = JSON.toJSONString(someThing);
        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(requestBody);
            stringEntity.setContentType("text/json");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            httpPost.setEntity(stringEntity);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            //执行上下文可以在本地自定义。
            HttpClientContext context = HttpClientContext.create();
            //设置本地上下文级别的上下文属性

            System.out.println("Execution Request " + httpPost.getURI());
            try {
                CloseableHttpResponse response = httpClient.execute(httpPost, context);
                System.out.println("===============================================");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("===============================================");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
