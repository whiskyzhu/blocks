package com.blocks.cache.redis.pubsub;

import redis.clients.jedis.JedisPubSub;

import java.util.logging.Logger;

/**
 * 监听到订阅频道接受到消息时的回调 (onMessage )
 * 监听到订阅模式接受到消息时的回调 (onPMessage)
 * 取消订阅频道时的回调( onUnsubscribe )
 * 订阅频道时的回调( onSubscribe )
 * 订阅频道模式时的回调 ( onPSubscribe )
 * 取消订阅模式时的回调( onPUnsubscribe )
 *
 * Created by lotus on 2017/1/3.
 */
public abstract class JedisMessageListener extends JedisPubSub {
    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * 监听到订阅频道接受到消息时的回调 (onMessage )
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        LOGGER.info("channel " + channel +" received message:" + message );
    }

    /**
     * 监听到订阅模式接受到消息时的回调 (onPMessage)
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    /**
     * 订阅频道时的回调( onSubscribe )
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    /**
     * 取消订阅频道时的回调( onUnsubscribe )
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    /**
     * 取消订阅模式时的回调( onPUnsubscribe )
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    /**
     * 订阅频道模式时的回调 ( onPSubscribe )
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
