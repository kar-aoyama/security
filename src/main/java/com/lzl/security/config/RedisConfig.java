package com.lzl.security.config;


import com.lzl.security.properties.RedisProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;

/**
 * @author lzl
 * @ClassName RedisConfig
 * @date: 2021/4/13 下午4:43
 * @Description:
 */
@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    public LettuceConnectionFactory defaultConnectionFactory(RedisProperties redisProperties) {
        /* ========= 基本配置 ========= */
        RedisStandaloneConfiguration configuration = getStandaloneConfig(redisProperties);

        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(redisProperties.getMaxActive());
        genericObjectPoolConfig.setMinIdle(redisProperties.getMinIdle());
        genericObjectPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        //genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());

        /* ========= lettuce pool ========= */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
                LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        builder.commandTimeout(Duration.ofSeconds(redisProperties.getTimeout()));
        LettuceConnectionFactory connectionFactory =
                new LettuceConnectionFactory(configuration, builder.build());
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    private static RedisStandaloneConfiguration getStandaloneConfig(RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        return config;
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate() {
        LettuceConnectionFactory lettuceConnectionFactory = defaultConnectionFactory(redisProperties);
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
