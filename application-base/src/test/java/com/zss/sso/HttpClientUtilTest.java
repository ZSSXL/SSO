package com.zss.sso;

import com.zss.base.util.HttpUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 15:40
 * @desc HttpClient工具测试
 */
public class HttpClientUtilTest {

    public static void main(String[] args) {
        HttpClientUtilTest test = new HttpClientUtilTest();
        System.out.println("====================================");
        test.doPostTest();
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
}
