package com.zss.sso.util;

import com.zss.sso.config.RedisPool;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/8/10 14:12
 * @desc Redis - 工具类
 */
@SuppressWarnings("unused")
@Component
public class RedisUtil {

    private final RedisPool redisPool;

    @Autowired
    public RedisUtil(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    /**
     * redis - SET
     *
     * @param key   键
     * @param value 值
     * @return SET结果
     */
    public String set(String key, String value) {
        StatefulRedisConnection<String, String> redis = redisPool.getRedis();
        if (redis != null) {
            RedisCommands<String, String> sync = redis.sync();
            String set = sync.set(key, value);
            redisPool.returnRedis(redis);
            return set;
        } else {
            return "";
        }
    }

    /**
     * redis - get
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        StatefulRedisConnection<String, String> redis = redisPool.getRedis();
        if (redis != null) {
            RedisCommands<String, String> sync = redis.sync();
            String get = sync.get(key);
            redisPool.returnRedis(redis);
            return get;
        } else {
            return "";
        }
    }

    /**
     * redis - setEx
     *
     * @param key   键
     * @param value 值
     * @param ex    过期时间，单位秒
     * @return SET结果
     */
    public String set(String key, String value, Long ex) {
        StatefulRedisConnection<String, String> redis = redisPool.getRedis();
        if (redis != null) {
            RedisCommands<String, String> sync = redis.sync();
            SetArgs setArgs = SetArgs.Builder.ex(ex);
            String set = sync.set(key, value, setArgs);
            redisPool.returnRedis(redis);
            return set;
        } else {
            return "";
        }
    }

    /**
     * redis - del
     *
     * @param key 键
     * @return 删除结果
     */
    public Long del(String key) {
        StatefulRedisConnection<String, String> redis = redisPool.getRedis();
        if (redis != null) {
            RedisCommands<String, String> sync = redis.sync();
            Long del = sync.del(key);
            redisPool.returnRedis(redis);
            return del;
        } else {
            return 0L;
        }
    }
}
