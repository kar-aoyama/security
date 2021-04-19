package com.lzl.security.util;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author lzl
 * @ClassName RedisUtil
 * @date: 2021/4/13 下午4:26
 * @Description:
 */
@Configuration
public class RedisUtil implements ApplicationContextAware {


    static RedissonClient redissonClient;

    @Value("${redisson.url}")
    private String url;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redissonClient = applicationContext.getBean(RedissonClient.class);
    }

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(url);
        return Redisson.create(config);
    }

    public static boolean lock(String key) {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock();
    }

    public static boolean lock(String key, long time, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock(time, timeUnit);
    }

    public static void unLock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isLocked()) {
            lock.unlock();
        }
    }








}
