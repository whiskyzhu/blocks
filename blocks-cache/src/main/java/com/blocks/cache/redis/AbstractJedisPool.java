package com.blocks.cache.redis;

import redis.clients.jedis.JedisPool;

/**
 * Created by lotus on 2016/12/26.
 */
public class AbstractJedisPool {
    private JedisPool jedisPool;

    protected AbstractJedisPool(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
