package com.kunlun.utils;

import com.google.common.net.HostAndPort;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis连接工具类
 * Created by kunlun on 2017/3/27.
 */
public class Redis implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(Redis.class);

    private static final long serialVersionUID = -4671968117849288370L;

    //连接池
    static JedisPool pool;

    static {
        Minerable config = new Miner().getMiner("hcon.base", "redis");
        String connect = config.getString("connect", "localhost");
        HostAndPort hostAndPort = HostAndPort.fromString(connect);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        String host = hostAndPort.getHostText();
        int port = hostAndPort.getPort();
        int timeout = config.getInt("timeout", 2000);
        String password = config.getString("auth");
        logger.info("redis hostAndPort:{},redis password:{}", hostAndPort, password);
        int database = config.getInt("database", 0);
        int maxClients = config.getInt("maxClients", 500);
        jedisPoolConfig.setMaxTotal(maxClients);
        pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        logger.info("redis 初始化完成->");
    }


    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : value;
    }

    public static String get(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 用于获取与字段中存储的键哈希相关联的值
     *
     * @param key
     * @param field
     * @return
     */
    public static String hget(String key, String field) {
        Jedis jedis = jedis();
        try {
            return jedis.hget(key, field);
        } finally {
            jedis.close();
        }
    }

    /**
     * 存入redis
     *
     * @param key
     * @param names
     * @return
     */
    public static Long lpush(String key, String... names) {
        Jedis jedis = jedis();
        try {
            return jedis.lpush(key, names);
        } finally {
            jedis.close();
        }

    }

    /**
     * 删除元素并且返回保存在第一个位置的值
     *
     * @param key
     * @return
     */
    public static String lpop(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.lpop(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * lget固定用法 取出所有值
     *
     * @param key
     * @return
     */
    public static List lget(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.lrange(key, 0, -1);
        } finally {
            jedis.close();
        }
    }

    /**
     * lrange用法  example : lrange key start end
     * end  ：-1  表示最后一个元素  -2  表示倒数第二个
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List lrange(String key, long start, long end) {
        Jedis jedis = jedis();
        try {
            return jedis.lrange(key, start, end);
        } finally {
            jedis.close();
        }
    }

    /**
     * redis事务处理
     *
     * @param key
     * @return
     */
    public static String getAndDel(String key) {
        Jedis jedis = jedis();
        try {
            Transaction multi = jedis.multi();
            multi.get(key);
            multi.del(key);
            List<Object> result = multi.exec();
            return (String) result.get(0);
        } finally {
            jedis.close();
        }
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public static Long del(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 销毁
     */
    public static void destory() {
        pool.destroy();
    }


    //获得redis连接实例
    public static Jedis jedis() {
        return pool.getResource();
    }


    public static interface JedisOp {
        Object exec(Jedis jedis);
    }

    public static void expire(String key, long expireValue, TimeUnit timeUnit) {
        Jedis jedis = jedis();
        try {
            jedis.expire(key, (int) timeUnit.toSeconds(expireValue));
        } finally {
            jedis.close();
        }
    }

    public static abstract class JedisVoidOp implements JedisOp {
        @Override
        public Object exec(Jedis jedis) {
            call(jedis);
            return null;
        }

        protected abstract void call(Jedis jedis);
    }
}
