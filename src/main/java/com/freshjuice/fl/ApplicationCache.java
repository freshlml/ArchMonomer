package com.freshjuice.fl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Configuration
@PropertySource(value={"classpath:redis.properties"})
public class ApplicationCache {

    //RedisCacheManager
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory jedisClusterConectionFactory) {
        //RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(jedisClusterConectionFactory);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        CacheManager cacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return cacheManager;
    }

    //redisTemplate
    @Bean
    public RedisTemplate<Serializable, Serializable> redisTemplate() {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<Serializable, Serializable>();
        redisTemplate.setConnectionFactory(jedisClusterConectionFactory());
        return redisTemplate;
    }

    //RedisConnectionFactory using JedisConnectionFactory
    @Bean
    public JedisConnectionFactory jedisClusterConectionFactory() {
        JedisConnectionFactory jedisConnectionFactory =
                new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
        return jedisConnectionFactory;
    }

    @Value("${jedis.cluster.node1.host:#{localhost}}")
    private String node1Host;
    @Value("${jedis.cluster.node1.port:#{6379}}")
    private int node1Port;
    @Value("${jedis.cluster.node2.host:#{localhost}}")
    private String node2Host;
    @Value("${jedis.cluster.node2.port:#{6379}}")
    private int node2Port;
    @Value("${jedis.cluster.node3.host:#{localhost}}")
    private String node3Host;
    @Value("${jedis.cluster.node3.port:#{6379}}")
    private int node3Port;
    //RedisClusterConfiguration
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        List<RedisNode> nodes = new ArrayList<RedisNode>();
        nodes.add(new RedisNode(node1Host, node1Port));
        nodes.add(new RedisNode(node2Host, node2Port));
        nodes.add(new RedisNode(node3Host, node3Port));
        redisClusterConfiguration.setClusterNodes(nodes);
        return redisClusterConfiguration;
    }

    @Value("${jedis.pool.maxTotal:#{1000}}")
    private int maxTotal;
    @Value("${jedis.pool.minIdle:#{50}}")
    private int minIdle;
    @Value("${jedis.pool.maxIdle:#{100}}")
    private int maxIdle;
    @Value("${jedis.pool.maxWaitMillis:#{10000}}")
    private long maxWaitMillis;
    //JedisPoolConfig
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return jedisPoolConfig;
    }

    /*
    *using lettuce 替代 Jedis 实践
    *
    */


}
