package com.taotao.jedis;


import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {
    @Test
    public void testJedis()throws Exception{
        Jedis jedis = new Jedis("192.168.25.128",6379);
        jedis.set("jedis-key","1234");
        String result = jedis.get("jedis-key");
        System.out.println(result);
        jedis.close();
    }

    @Test
    public void testJedisPool(){
        JedisPool jedisPool = new JedisPool("192.168.25.128",6379);//单例
        Jedis jedis = jedisPool.getResource();//在方法内使用
        String result = jedis.get("jedis-key");
        System.out.println(result);
        jedis.close();//一定要关闭连接
        jedisPool.close();
    }

    @Test
    public void testJedisCluster()throws Exception{
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.25.128",7001));
        nodes.add(new HostAndPort("192.168.25.128",7002));
        nodes.add(new HostAndPort("192.168.25.128",7003));
        nodes.add(new HostAndPort("192.168.25.128",7004));
        nodes.add(new HostAndPort("192.168.25.128",7005));
        nodes.add(new HostAndPort("192.168.25.128",7006));

        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("cluster-test","hello jedis cluster");
        String result = jedisCluster.get("cluster-test");
        System.out.println(result);
        jedisCluster.close();
    }
}
