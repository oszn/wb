package com.example.demo.myredis;

import com.example.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class myredis {
    //例如某个Service里面只需要引用RedisTemplate类即可：
    @Autowired
    private static RedisTemplate redisTemplate;
    //某个service方法中，直接调用redisTemplate操作redis的set集合，储存key和value
    @Test
    public void cacheAround() throws Throwable {
        //直接调用redisTemplate操作redis的set集合，储存key和value
        redisTemplate.opsForSet().add("l","1");
        //这里不需要，关心redisTemplate里面配置的是连接池，还是哨兵，还是cluster。

    }
}
