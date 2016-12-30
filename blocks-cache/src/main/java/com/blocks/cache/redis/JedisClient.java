package com.blocks.cache.redis;

import java.util.Map;
import java.util.Set;

/**
 * Created by lotus on 2016/12/26.
 */
public interface JedisClient extends BaseJedisClient{

    public void destory();

    public <T> T execute(JedisAction<T> action);

    /**
     * 修改 key 的名称
     * 改名成功时提示 OK ，失败时候返回一个错误。
     * @param key
     * @return 改名成功时提示 OK ，失败时候返回一个错误。
     */
    public String rename(String key, String newKey);

}
