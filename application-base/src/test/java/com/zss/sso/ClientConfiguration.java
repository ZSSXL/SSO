package com.zss.sso;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 11:16
 * @desc 这个是HttpClient官方的一个示例
 */
public class ClientConfiguration {

    public final static void main(String[] args) throws Exception {

        // 使用自定义消息解析器/编写器自定义HTTP方式
        //消息从数据流中解析并写出。
        HttpMessageParserFactory<HttpResponse> responseParserFactory =
                new DefaultHttpResponseParserFactory() {

                    @Override
                    public HttpMessageParser<HttpResponse> create(
                            SessionInputBuffer buffer, MessageConstraints constraints) {
                        LineParser lineParser = new BasicLineParser() {

                            @Override
                            public Header parseHeader(final CharArrayBuffer buffer) {
                                try {
                                    return super.parseHeader(buffer);
                                } catch (ParseException ex) {
                                    return new BasicHeader(buffer.toString(), null);
                                }
                            }

                        };
                        return new DefaultHttpResponseParser(
                                buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                            @Override
                            protected boolean reject(final CharArrayBuffer line, int count) {
                                // try to ignore all garbage preceding a status line infinitely
                                return false;
                            }

                        };
                    }

                };
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        //使用自定义连接工厂自定义以下过程
        //初始化传出HTTP连接。 除了标准连接
        //配置参数HTTP连接工厂可以定义消息
        //各个连接使用的解析器/编写器例程。
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);

        //客户端HTTP连接对象在完全初始化时可以绑定到
        //一个任意的网络套接字。 网络套接字初始化的过程，
        //控制其与远程地址的连接以及与本地地址的绑定
        //由连接套接字工厂提供。

        //安全连接的SSL上下文可以基于
        //系统或应用程序特定的属性。
        SSLContext sslcontext = SSLContexts.createSystemDefault();

        //创建自定义连接套接字工厂的注册表以供支持
        //协议方案。
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        //使用自定义DNS解析器覆盖系统DNS解析。
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("myhost")) {
                    return new InetAddress[]{InetAddress.getByAddress(new byte[]{127, 0, 0, 1})};
                } else {
                    return super.resolve(host);
                }
            }

        };

        //创建具有自定义配置的连接管理器。
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);

        //创建Socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        //配置连接管理器以使用套接字配置
        //默认情况下或针对特定主机。
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
        //闲置1秒钟后验证连接
        connManager.setValidateAfterInactivity(1000);

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
        connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

        //配置永久连接的最大总数或每个路由限制
        //可以保留在池中或由连接管理器租用。
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

        //如有必要，请使用自定义Cookie存储。
        CookieStore cookieStore = new BasicCookieStore();
        //如有必要，请使用自定义凭据提供程序。
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //创建全局请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        //使用给定的自定义依赖项和配置创建HttpClient。
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(new HttpHost("myproxy", 8080))
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        try {
            HttpGet httpget = new HttpGet("http://httpbin.org/get");
            //请求配置可以在请求级别被覆盖。
            //它们将优先于客户端级别的一组。
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .setProxy(new HttpHost("myotherproxy", 8080))
                    .build();
            httpget.setConfig(requestConfig);

            //执行上下文可以在本地自定义。
            HttpClientContext context = HttpClientContext.create();
            //设置本地上下文级别的上下文属性
            //优先于在客户端级别设置的优先级。
            context.setCookieStore(cookieStore);
            context.setCredentialsProvider(credentialsProvider);

            System.out.println("executing request " + httpget.getURI());
            CloseableHttpResponse response = httpclient.execute(httpget, context);

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

