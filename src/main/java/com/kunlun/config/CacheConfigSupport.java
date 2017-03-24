package com.kunlun.config;

import com.google.common.net.HostAndPort;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
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
        redisConnectionFactory.setPort(hostAndPort.getPortOrDefault(6369));
        redisConnectionFactory.setTimeout(config.getInt("timeout", 2000));
        redisConnectionFactory.setPassword(config.getString("auth"));
        redisConnectionFactory.setDatabase(config.getInt("database", 1));
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return redisConnectionFactory;
    }
}
