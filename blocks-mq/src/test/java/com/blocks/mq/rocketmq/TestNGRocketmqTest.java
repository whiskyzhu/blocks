package com.blocks.mq.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.blocks.TestNGBaseTest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lotus on 2016/12/28.
 */
public class TestNGRocketmqTest extends TestNGBaseTest {

    @Autowired
    private RocketmqProducer rocketmqProducer;

    @Autowired
    private RocketmqPushConsumer rocketmqPushConsumer;

    private String topic = "MyTopic";
    private String tag = "MyTag";

    @BeforeClass
    public void beforeClass(){
        //注解的方法将只运行一次先行先试在当前类中的方法调用
        //一般针对的资源，如数据库连接
    }

    @AfterClass
    public void afterClass(){
        //注解的方法将只运行一次后已经运行在当前类中的所有测试方法。
    }

    @BeforeTest
    public void beforeTest(){
        //注解的方法将被运行之前的任何测试方法属于内部类的 <test>标签的运行
    }

    @AfterTest
    public void afterTest(){
        //注解的方法将被运行后，所有的测试方法，属于内部类的<test>标签的运行
    }

    @BeforeMethod
    public void beforeMethod(){
        //注解的方法将每个测试方法之前运行
    }

    @AfterMethod
    public void afterMethod(){
        //被注释的方法将被运行后，每个测试方法
    }

    @Test
    public void testProducer(){
        String sendMsg = "hello";
        Object obj = null;
        Message msg = new Message(topic, tag, (JSONObject.fromObject(obj)).toString().getBytes());
        try {
            Message msg1 = new Message("TopicTest1",// topic
                    "TagA",// tag
                    "OrderID001",// key
                    ("Hello MetaQ").getBytes());// body
            SendResult sendResult1 = rocketmqProducer.getDefaultMQProducer().send(msg1);
            System.out.println(sendResult1);

            Message msg2 = new Message("TopicTest2",// topic
                    "TagB",// tag
                    "OrderID0034",// key
                    ("Hello MetaQ").getBytes());// body
            SendResult sendResult2 = rocketmqProducer.getDefaultMQProducer().send(msg2);
            System.out.println(sendResult2);

            Message msg3 = new Message("TopicTest3",// topic
                    "TagC",// tag
                    "OrderID061",// key
                    ("Hello MetaQ").getBytes());// body
            SendResult sendResult3 = rocketmqProducer.getDefaultMQProducer().send(msg3);
            System.out.println(sendResult3);

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubscrible(){
        try {
            /**
             * 订阅指定topic下tags分别等于TagA或TagC或TagD
             */
            rocketmqPushConsumer.getDefaultMQPushConsumer().subscribe("TopicTest1", "TagA || TagC || TagD");
            /**
             * 订阅指定topic下所有消息<br>
             * 注意：一个consumer对象可以订阅多个topic
             */
            rocketmqPushConsumer.getDefaultMQPushConsumer().subscribe("TopicTest2", "*");

            rocketmqPushConsumer.getDefaultMQPushConsumer().registerMessageListener(new MessageListenerConcurrently() {
                /**
                 *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                 * @param list
                 * @param consumeConcurrentlyContext
                 * @return
                 */
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    System.out.println(Thread.currentThread().getName()
                            + " Receive New Messages: " + list.size());

                    MessageExt msg = list.get(0);
                    if (msg.getTopic().equals("TopicTest1")) {
                        // 执行TopicTest1的消费逻辑
                        if (msg.getTags() != null && msg.getTags().equals("TagA")) {
                            // 执行TagA的消费
                            System.out.println(new String(msg.getBody()));
                        } else if (msg.getTags() != null
                                && msg.getTags().equals("TagC")) {
                            // 执行TagC的消费
                        } else if (msg.getTags() != null
                                && msg.getTags().equals("TagD")) {
                            // 执行TagD的消费
                        }
                    } else if (msg.getTopic().equals("TopicTest2")) {
                        System.out.println(new String(msg.getBody()));
                    }
                    // 如果没有return success ，consumer会重新消费该消息，直到return success
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            /**
             * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             */
            rocketmqPushConsumer.getDefaultMQPushConsumer().start();

            System.out.println("Consumer Started.");

        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }


}
