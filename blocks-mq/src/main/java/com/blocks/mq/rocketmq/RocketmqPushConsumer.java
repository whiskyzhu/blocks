package com.blocks.mq.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 当前是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。
 * 但是实际PushConsumer内部是使用长轮询Pull方式从MetaQ服务器拉消息，然后再回调用户Listener方法
 *
 * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
 * 注意：ConsumerGroupName需要由应用来保证唯一
 * Created by lotus on 2017/1/5.
 */
public class RocketmqPushConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
     */
    private DefaultMQPushConsumer defaultMQPushConsumer;
    /**
     * 名称服务，接收broker的请求，注册broker的路由信息
     */
    private String namesrvAddr;
    /**
     * 注意：ConsumerGroupName需要由应用来保证唯一
     */
    private String consumerGroup;
    private String instanceName;

    public void init(){
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setInstanceName(instanceName + String.valueOf(System.currentTimeMillis()));
    }

    /**
     *
     * @param messageListenerConcurrently
     * @param topic 订阅指定topic下所有消息
     * @param subExpression eg:"TagA || TagC || TagD"
     * @throws MQClientException
     */
    public void subscrible(MessageListenerConcurrently messageListenerConcurrently, String topic, String subExpression) throws MQClientException {
        defaultMQPushConsumer.subscribe(topic, subExpression);

        /**
         * 程序第一次启动从消息队列头取数据
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         */
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.registerMessageListener(messageListenerConcurrently);
        // 设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        // Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
        defaultMQPushConsumer.start();

    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer() {
        return defaultMQPushConsumer;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
