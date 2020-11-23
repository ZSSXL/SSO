package com.zss.sso;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.nio.charset.CodingErrorAction;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 13:26
 * @desc
 */
public class HttpClient {

    public final static void main(String[] args) throws Exception {

        //创建具有自定义配置的连接池管理器。
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

        //创建Socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(5000)
                .build();
        //配置连接管理器以使用套接字配置
        //默认情况下或针对特定主机。
        connManager.setDefaultSocketConfig(socketConfig);
        //闲置1秒钟后验证连接

        //创建消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        //创建连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        //将连接管理器配置为使用连接配置
        //默认情况下或针对特定主机。
        connManager.setDefaultConnectionConfig(connectionConfig);

        //配置永久连接的最大总数或每个路由限制
        //可以保留在池中或由连接管理器租用。
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);

        //如有必要，请使用自定义Cookie存储。 - 默认
        CookieStore cookieStore = new BasicCookieStore();
        //创建全局请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .build();

        //使用给定的自定义依赖项和配置创建HttpClient。
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        try {
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8881/one");
            //请求配置可以在请求级别被覆盖。
            //它们将优先于客户端级别的一组。
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            httpPost.setConfig(requestConfig);

            String someThing = "I am post String";
            String requestBody = JSON.toJSONString(someThing);
            StringEntity stringEntity = new StringEntity(requestBody);
            stringEntity.setContentType("text/json");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            httpPost.setEntity(stringEntity);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");

            //执行上下文可以在本地自定义。
            HttpClientContext context = HttpClientContext.create();
            //设置本地上下文级别的上下文属性
            //优先于在客户端级别设置的优先级。
            context.setCookieStore(cookieStore);

            System.out.println("executing request " + httpPost.getURI());
            CloseableHttpResponse response = httpclient.execute(httpPost, context);

            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("----------------------------------------");

                //一旦执行了请求，本地上下文可以
                //用于检查更新状态和受影响的各种对象
                //由请求执行。

                // 最后执行的请求
                context.getRequest();
                // 执行路线
                context.getHttpRoute();
                // 目标身份验证状态
                context.getTargetAuthState();
                // 代理验证状态
                context.getProxyAuthState();
                // Cookie来源
                context.getCookieOrigin();
                // 使用的Cookie规格
                context.getCookieSpec();
                // 用户安全令牌
                context.getUserToken();
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
