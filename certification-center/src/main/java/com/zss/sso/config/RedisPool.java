package com.zss.sso.config;

import com.zss.sso.properties.LettucePoolProperties;
import com.zss.sso.properties.LettuceProperties;
import com.zss.sso.properties.LettuceSingleProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/8/11 15:38
 * @desc Redis连接池 - V2版本
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class RedisPool {

    private final LettuceProperties lettuceProperties;

    /**
     * Redis连接池
     */
    private static GenericObjectPool<StatefulRedisConnection<String, String>> genericObjectPool;

    @Autowired
    public RedisPool(LettuceProperties lettuceProperties) {
        this.lettuceProperties = lettuceProperties;
    }


    public StatefulRedisConnection<String, String> getRedis() {
        LettucePoolProperties pool = lettuceProperties.getPool();
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxTotal(pool.getMaxTotal());
        poolConfig.setTestOnBorrow(pool.getTestOnBorrow());
        poolConfig.setTestOnReturn(pool.getTestOnReturn());
        poolConfig.setTestOnCreate(pool.getTestOnCreate());
        poolConfig.setMaxWaitMillis(pool.getMaxWaitMills());

        LettuceSingleProperties single = lettuceProperties.getSingle();
        // 创建连接信息
        RedisURI redisUri = RedisURI.builder()
                .withHost(single.getHost())
                .withPort(single.getPort())
                .withDatabase(single.getDatabase())
                .withTimeout(Duration.of(single.getTimeout(), ChronoUnit.SECONDS))
                .build();

        // 创建客户端连接
        RedisClient redisClient = RedisClient.create(redisUri);

        genericObjectPool = ConnectionPoolSupport
                .createGenericObjectPool(redisClient::connect, poolConfig);

        try {
            return genericObjectPool.borrowObject();
        } catch (Exception e) {
            log.error("Redis Connection Pool Exception : [{}]", e.getMessage());
            return null;
        }
    }

    /**
     * 将连接实例对象归还给对象连接池
     *
     * @param redis 连接实例
     */
    public void returnRedis(StatefulRedisConnection<String, String> redis) {
        genericObjectPool.returnObject(redis);
    }

    /**
     * 获取连接池信息
     * NumActive : 对象池中有对象对象是活跃的
     * NumIdle : 对象池中有多少对象是空闲的
     */
    public void getPoolMessage() {
        int numActive = genericObjectPool.getNumActive();
        int numIdle = genericObjectPool.getNumIdle();
        log.info("NumActive : [" + numActive + "], " +
                "NumIdle : [" + numIdle + "]");
    }
}
