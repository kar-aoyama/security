package com.lzl.security;

import com.lzl.security.enums.UrlType;
import com.lzl.security.util.ApplicationHolder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        Set<String> authorityUrls = ApplicationHolder.getAuthorityUrls();
        BoundHashOperations boundHashOperations =
                redisTemplate.boundHashOps(UrlType.AUTHORITY.name());
        Map<String, Set<String>> maps = new HashMap<>();
        maps.put(UrlType.AUTHORITY.name(), authorityUrls);
        boundHashOperations.putAll(maps);
    }

    @Test
    void method() {
        BoundHashOperations<String, String, Set<String>> ops = redisTemplate.boundHashOps(UrlType.AUTHORITY.name());
        Set<String> s = ops.get(UrlType.AUTHORITY.name());
        System.out.println(s.toArray()[0]);
    }

}
