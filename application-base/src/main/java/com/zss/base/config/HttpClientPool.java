package com.zss.base.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.util.Properties;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 10:57
 * @desc HttpClient连接池
 */
@Slf4j
public class HttpClientPool {

    private static final int SOCKET_TIMEOUT;
    private static final int CONNECT_TIMEOUT;
    private static final int DEFAULT_MAX_PER_ROUTE;
    private static final int CONNECTION_REQUEST_TIMEOUT;
    private static final Integer MAX_TOTAL;

    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = HttpClientPool.class
                .getClassLoader()
                .getResourceAsStream("config/http-pool-config.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            log.error("加载配置文件失败: [{}]", e.getMessage());
        }
        SOCKET_TIMEOUT = Integer.parseInt(properties.getProperty("http.socket_timeout"));
        CONNECT_TIMEOUT = Integer.parseInt(properties.getProperty("http.connect_timeout"));
        DEFAULT_MAX_PER_ROUTE = Integer.parseInt(properties.getProperty("http.default_max_per_route"));
        CONNECTION_REQUEST_TIMEOUT = Integer.parseInt(properties.getProperty("http.connection_request_timeout"));
        MAX_TOTAL = Integer.valueOf(properties.getProperty("http.max_total"));
    }


    public static CloseableHttpClient getHttpClient() {
        // 创建具有自定义配置的连接池管理器。
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

        // 创建Socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(SOCKET_TIMEOUT)
                .build();
        // 配置连接管理器以使用套接字配置
        connManager.setDefaultSocketConfig(socketConfig);
        // 创建连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .build();
        // 将连接管理器配置为使用连接配置
        // 默认情况下或针对特定主机。
        connManager.setDefaultConnectionConfig(connectionConfig);

        // 配置永久连接的最大总数或每个路由限制
        // 可以保留在池中或由连接管理器租用。
        connManager.setMaxTotal(MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);

        // 如有必要，请使用自定义Cookie存储
        CookieStore cookieStore = new BasicCookieStore();
        // 创建全局请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .build();

        // 使用给定的自定义依赖项和配置创建HttpClient。
        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }
}
