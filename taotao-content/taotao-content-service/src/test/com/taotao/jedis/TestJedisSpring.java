package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
    @Test
    public void testJedisPool()throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient =  applicationContext.getBean(JedisClient.class);
        jedisClient.set("pitian","mytest");
        String result = jedisClient.get("pitian");
        System.out.println(result);
    }
}
