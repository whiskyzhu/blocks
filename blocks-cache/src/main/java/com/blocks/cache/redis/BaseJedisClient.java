package com.blocks.cache.redis;

import java.util.Map;
import java.util.Set;

/**
 * Created by lotus on 2016/12/30.
 */
public interface BaseJedisClient {
    /**
     * 将序列化字符串对象值value关联到key，如果key已经持有其他值，SET就覆旧值
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value);

    /**
     * 将序列化字符串对象值value关联到key，如果key已经持有其他值，SET就覆旧值，有效时间为expire秒
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(final String key, final String value, final int expire);

    public String get(final String key);



    /**
     * 在key存在时删除key，如果键被删除成功，命令执行后输出 1，否则将输出 0
     * @param key
     * @return
     */
    public Long del(final String key);

    public long del(final byte[] key);

    /**
     * 删除缓存的对象
     *
     * @param key
     * @return
     */
    public long delObject(final String key);

    /**
     * 保存序列化的对象
     * @param key
     * @param value
     * @return
     */
    public String setObject(final String key, final Object value);

    /**
     * 保存序列化的对象（带有效时间）,有效时间为expire秒
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String setObject(final String key, final Object value, final int expire);

    public String set(final byte[] key, final Object value);

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long hset(final String key, final String field, final String value);

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖，expire秒后失效
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long hset(final String key, final String field, final String value, final int expire);

    public long hsetObject(final String key, final String field, final Object value);

    public long hsetObject(final String key, final String field, final Object value, final int expire);

    /**
     * 返回哈希表key中给定域field的值。
     * @param key
     * @param field
     * @return
     */
    public String hget(final String key, final String field);

    public Object hgetObject(final String key, final String field);

    /**
     * 返回哈希表 key 中，所有的域和值。
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(final String key);

    public Map<String, Object> hgetAllObject(final String key);

    /**
     * 同时将多个域-值对设置到哈希表key中。如果key不存在，一个空哈希表被创建并执行HMSET操作。
     * @param key
     * @param hash
     * @return
     */
    public String hmset(final String key, final Map<String, String> hash);

    /**
     * 同时将多个域-值对设置到哈希表key中。如果key不存在，一个空哈希表被创建并执行HMSET操作。expire秒后失效
     * @param key
     * @param hash
     * @param expire
     * @return
     */
    public String hmset(final String key, final Map<String, String> hash, final int expire);

    /**
     * 删除哈希表key中的指定域，不存在的域将被忽略。
     * @param key
     * @param field
     * @return
     */
    public long hdel(final String key, final String... field);

    /**
     * 在指定Key所关联的List
     * Value的头部插入参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的头部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(final String key, final Object value);

    /**
     * 在指定Key所关联的List
     * Value的尾部插入参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的尾部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。
     *
     * @param key
     * @param value
     * @return
     */
    public Long rpush(final String key, final Object value);

    /**
     * 返回指定Key关联的链表中元素的数量，如果该Key不存在，则返回0。如果与该Key关联的Value的类型不是链表，则返回相关的错误信息。
     *
     * @param key
     * @return
     */
    public Long llen(final String key);

    /**
     * 返回并弹出指定Key关联的链表中的第一个元素，即头部元素。如果该Key不存，返回null。
     *
     * @param key
     * @return
     */
    public Object lpop(final String key);

    /**
     * 返回并弹出指定Key关联的链表中的最后一个元素，即尾部元素。如果该Key不存，返回nil。
     *
     * @param key
     * @return
     */
    public Object rpop(final String key);

    /**
     * 将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     *
     * @param key
     * @return
     */
    public Long incr(final String key);

    /**
     * 带有效期的递增计数器
     *
     * @param key
     * @param expire
     * @return
     */
    public Long incr(final String key, final int expire);

    /**
     * 将 key 中储存的数字加上指定的增量值
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(final String key, final long integer);

    /**
     * 将 key 中储存的数字值减一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     *
     * @param key
     * @return
     */
    public Long decr(final String key);

    /**
     * 带有效期的递减计数器
     *
     * @param key
     * @param expire
     * @return
     */
    public Long decr(final String key, final int expire);

    /**
     * key 所储存的值减去给定的减量值
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(final String key, final long integer);

    /**
     * 为指定的 key 追加值
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key
     * @param value
     * @return
     */
    public Long append(final String key, final String value);

    /**
     * 为哈希表key 中的域field 的值加上增量increment。增量也可以为负数，相当于对给定域进行减法操作。 如果key
     * 不存在，一个新的哈希表被创建并执行HINCRBY 命令。 如果域field 不存在，那么在执行命令前，域的值被初始化为0 。
     * 对一个储存字符串值的域field 执行HINCRBY 命令将造成一个错误。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrBy(String key, String field, long value);

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean exists(final String key);

    /**
     * 给定 key 设置过期时间，expire秒
     * @param key
     * @param expire
     * @return
     */
    public Long expire(final String key, final int expire);

    /**
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     * @param key
     * @param unixTimestamp
     * @return
     */
    public Long expireAt(final String key, final Long unixTimestamp);

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param key
     * @return 符合给定模式的 key 列表 (Array)
     */
    public Set<String> keys(final String key);

    public Set<byte[]> bKeys(final String key);

    /**
     * 以秒为单位返回 key 的剩余过期时间 (TTL, time to live)
     * 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
     * @param key
     * @return
     */
    public Long ttl(final String key);

    /**
     * 返回 key 所储存的值的类型
     * 返回 key 的数据类型，数据类型有：
     none (key不存在)
     string (字符串)
     list (列表)
     set (集合)
     zset (有序集)
     hash (哈希表)
     * @param key
     * @return
     */
    public String type(String key);

    /**
     * 将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
     *
     * @param key
     * @param members
     * @return 当集合 key 不是集合类型时，返回一个错误。
     */
    public Long sadd(final String key, final String... members);

    /**
     * 移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素
     */
    public Long srem(final String key, final String... members);

    /**
     * 判断成员元素是否是集合的成员
     *
     * @param key
     * @param member
     * @return 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
     */
    public Boolean sismember(final String key, final String member);

    /**
     * 返回集合中的所有的成员
     * 不存在的集合 key 被视为空集合
     * @param key
     * @return 集合中的所有成员
     */
    public Set<String> smembers(final String key);

    /**
     * 返回集合中元素的数量
     * @param key
     * @return 集合的数量。 当集合 key 不存在时，返回 0
     */
    public Long scard(final String key);

}
