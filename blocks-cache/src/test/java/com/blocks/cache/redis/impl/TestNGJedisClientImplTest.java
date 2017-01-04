package com.blocks.cache.redis.impl;

import com.blocks.TestNGBaseTest;
import com.blocks.cache.redis.JedisClient;
import com.blocks.cache.redis.pubsub.JedisMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Created by lotus on 2016/12/28.
 */
public class TestNGJedisClientImplTest extends TestNGBaseTest {

    @Autowired
    private JedisClient JedisClientImpl;

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
    public void testString(){
        JedisClientImpl.set("stringkey1", "stringValue1");
        Assert.assertEquals(JedisClientImpl.get("stringkey1"), "stringValue1");

        Assert.assertEquals(JedisClientImpl.del("stringkey1").longValue(), 1);

        JedisClientImpl.set("stringExpireKey1", "stringExpireValue1", 2);
        try {
            Thread.sleep(3000);
            Assert.assertNull(JedisClientImpl.get("stringExpireKey1"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JedisClientImpl.del("stringkey1");
    }

    @Test
    public void testList(){
        // 栈
        JedisClientImpl.del("list1");

        Assert.assertEquals(JedisClientImpl.lpush("list1", "value1").longValue(), 1);
        Assert.assertEquals(JedisClientImpl.lpush("list1", "value2").longValue(), 2);
        Assert.assertEquals(JedisClientImpl.lpush("list1", "value3").longValue(), 3);

        Assert.assertEquals(JedisClientImpl.llen("list1").longValue(), 3);

        Assert.assertEquals(JedisClientImpl.lpop("list1").toString(), "value3");
        Assert.assertEquals(JedisClientImpl.llen("list1").longValue(), 2);

        JedisClientImpl.del("list1");
    }

    @Test
    public void testSet(){
        JedisClientImpl.del("set1");
        JedisClientImpl.sadd("set1", "value1", "value2", "value3");
        Assert.assertEquals(JedisClientImpl.scard("set1").longValue(), 3);
        Assert.assertEquals(JedisClientImpl.sismember("set1", "value1").booleanValue(), true);
        JedisClientImpl.srem("set1", "value1");
        Assert.assertEquals(JedisClientImpl.scard("set1").longValue(), 2);
        JedisClientImpl.del("set1");
    }

    @Test
    public void testHash(){
        JedisClientImpl.del("hsh1");
        JedisClientImpl.hset("hshKey1", "name", "zhangsan");
        JedisClientImpl.hset("hshKey1", "sex", "male");
        JedisClientImpl.hset("hshKey1", "age", "20");
        Assert.assertEquals(JedisClientImpl.hget("hshKey1", "sex"), "male");

        Map<String, String> map = JedisClientImpl.hgetAll("hshKey1");
        for(Map.Entry<String, String> entry : map.entrySet()){
            if (entry.getKey().equals("name")){
                Assert.assertEquals(JedisClientImpl.hget("hshKey1", "name"), entry.getValue());
            }else if (entry.getKey().equals("sex")){
                Assert.assertEquals(JedisClientImpl.hget("hshKey1", "sex"), entry.getValue());
            }if (entry.getKey().equals("age")){
                Assert.assertEquals(JedisClientImpl.hget("hshKey1", "age"), entry.getValue());
            }
        }

        JedisClientImpl.del("hsh1");
    }

    @Test
    public void testSubscrible(){
        String channel = "pubsub:queue";
        JedisClientImpl.subscrible(new JedisMessageListener() {
            /**
             * 监听到订阅频道接受到消息时的回调 (onMessage )
             *
             * @param channel
             * @param message
             */
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                System.out.println("channel " + channel +" received message:" + message );
                Assert.assertEquals(message, "hello!");
            }

            /**
             * 订阅频道时的回调( onSubscribe )
             *
             * @param channel
             * @param subscribedChannels
             */
            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println("subscrible channel" + channel +",subscribedChannels:"+subscribedChannels);
            }
        }, channel);

        JedisClientImpl.publish(channel, "hello!");
    }
}
