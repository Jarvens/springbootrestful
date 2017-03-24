package com.kunlun.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HostAndPort;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * Created by kunlun on 2017/3/24.
 */
@Configurable
@EnableCaching
public class CacheConfigSupport extends CachingConfigurerSupport implements Serializable {

    private static final long serialVersionUID = -4289591822539218189L;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        Minerable config = new Miner().getMiner("hcon.base", "redis");
        HostAndPort hostAndPort = HostAndPort.fromHost(config.getString("connect", "localhost"));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(config.getInt("maxClients", 500));
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(hostAndPort.getHostText());
        redisConnectionFactory.setPort(hostAndPort.getPortOrDefault(6379));
        redisConnectionFactory.setTimeout(config.getInt("timeout", 2000));
        redisConnectionFactory.setPassword(config.getString("auth"));
        redisConnectionFactory.setDatabase(config.getInt("database", 1));
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return redisConnectionFactory;
    }

    /**
     * 序列化对象
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate temlate = new StringRedisTemplate(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        temlate.setValueSerializer(jackson2JsonRedisSerializer);
        temlate.afterPropertiesSet();
        return temlate;
    }

    /**
     * 缓存支持
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setDefaultExpiration(7200);  //Set the default expire time  (second)
        return redisCacheManager;
    }
}
