package com.blocks.cache.redis;

/**
 * Created by lotus on 2016/12/30.
 */
public interface ShardedJedisClient extends BaseJedisClient{

    public void destory();

    public <T> T execute(ShardedJedisAction<T> action);
}
