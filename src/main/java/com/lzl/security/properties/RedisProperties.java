package com.lzl.security.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzl
 * @ClassName RedisProperties
 * @date: 2021/4/14 上午10:31
 * @Description:
 */
@Data
@Configuration
public class RedisProperties {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private String maxWait;

    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.lettuce.pool.timeout}")
    private long timeout;
}
