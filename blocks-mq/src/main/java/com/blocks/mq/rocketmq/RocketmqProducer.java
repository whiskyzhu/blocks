package com.blocks.mq.rocketmq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例
 *
 * Created by lotus on 2017/1/5.
 */
public class RocketmqProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例
     */
    private DefaultMQProducer defaultMQProducer;

    /**
     * ProducerGroupName需要由应用来保证唯一
     * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键
     * 因为服务器会回查这个Group下的任意一个Producer
     */
    private String producerGroup;

    /**
     * 名称服务，接收broker的请求，注册broker的路由信息
     */
    private String namesrvAddr;

    private String instanceName;

    public void init() throws MQClientException {
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(instanceName);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        defaultMQProducer.start();
    }

    public void destroy(){
        defaultMQProducer.shutdown();
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
