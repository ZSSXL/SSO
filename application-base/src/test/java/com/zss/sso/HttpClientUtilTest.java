package com.zss.sso;

import com.zss.base.util.HttpUtil;
import com.zss.base.util.MapUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 15:40
 * @desc HttpClient工具测试
 */
@SuppressWarnings("unused")
public class HttpClientUtilTest {

    public static void main(String[] args) {
        HttpClientUtilTest test = new HttpClientUtilTest();
        System.out.println("====================================");
        test.doGetCookieTest();
        System.out.println("====================================");
    }

    /**
     * get测试
     */
    public void doGetTest() {
        String uri = "http://localhost:8881/one/sso/";
        String[] params = {"year", "2020",
                "month", "11",
                "day", "23"};
        CloseableHttpResponse closeableHttpResponse = HttpUtil.doGet(uri, params);
        if (closeableHttpResponse != null) {
            System.out.println("----------------------------------------");
            System.out.println(closeableHttpResponse.getStatusLine());
            try {
                System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * post测试
     */
    public void doPostTest() {
        String uri = "http://localhost:8881/one";
        String content = "The world not enough";
        CloseableHttpResponse closeableHttpResponse = HttpUtil.doPost(uri, content);
        if (closeableHttpResponse != null) {
            System.out.println("----------------------------------------");
            System.out.println(closeableHttpResponse.getStatusLine());
            try {
                System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * get with DIY cookies
     */
    public void doGetCookieTest() {
        String uri = "http://localhost:8881/one/sso";
        HashMap<String, String> params = MapUtil.create("year", "2020",
                "month", "11",
                "day", "23");
        HashMap<String, String> cookiesMap = MapUtil.create(
                "ping", "pong",
                "hello", "hi",
                "type", "get");
        CloseableHttpResponse closeableHttpResponse = HttpUtil.doGet(uri, params, cookiesMap);
        if (closeableHttpResponse != null) {
            System.out.println("----------------------------------------");
            System.out.println(closeableHttpResponse.getStatusLine());
            try {
                System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * post with DIY cookies
     */
    public void doPostCookieTest() {
        String uri = "http://localhost:8881/one";
        String content = "The world not enough";
        HashMap<String, String> cookiesMap = MapUtil.create(
                "ping", "pong",
                "hello", "hi",
                "type", "post");
        CloseableHttpResponse closeableHttpResponse = HttpUtil.doPost(uri, content, cookiesMap);
        if (closeableHttpResponse != null) {
            System.out.println("----------------------------------------");
            System.out.println(closeableHttpResponse.getStatusLine());
            try {
                System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
        }
    }
}
