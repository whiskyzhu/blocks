package com.blocks.cache.redis.impl;

import com.blocks.TestNGBaseTest;
import com.blocks.cache.redis.ShardedJedisClient;
import com.blocks.cache.redis.ShardedJedisSentinelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Created by lotus on 2016/12/28.
 */
public class TestNGShardedJedisSentinelClientImplTest extends TestNGBaseTest {

    @Autowired
    private ShardedJedisSentinelClient sharedJedisSentinelClientImpl;

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
        sharedJedisSentinelClientImpl.set("stringkey1", "stringValue1");
        Assert.assertEquals(sharedJedisSentinelClientImpl.get("stringkey1"), "stringValue1");

        Assert.assertEquals(sharedJedisSentinelClientImpl.del("stringkey1").longValue(), 1);

        sharedJedisSentinelClientImpl.set("stringExpireKey1", "stringExpireValue1", 2);
        try {
            Thread.sleep(3000);
            Assert.assertNull(sharedJedisSentinelClientImpl.get("stringExpireKey1"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sharedJedisSentinelClientImpl.del("stringkey1");
    }

    @Test
    public void testList(){
        // 栈
        sharedJedisSentinelClientImpl.del("list1");

        Assert.assertEquals(sharedJedisSentinelClientImpl.lpush("list1", "value1").longValue(), 1);
        Assert.assertEquals(sharedJedisSentinelClientImpl.lpush("list1", "value2").longValue(), 2);
        Assert.assertEquals(sharedJedisSentinelClientImpl.lpush("list1", "value3").longValue(), 3);

        Assert.assertEquals(sharedJedisSentinelClientImpl.llen("list1").longValue(), 3);

        Assert.assertEquals(sharedJedisSentinelClientImpl.lpop("list1").toString(), "value3");
        Assert.assertEquals(sharedJedisSentinelClientImpl.llen("list1").longValue(), 2);

        sharedJedisSentinelClientImpl.del("list1");
    }

    @Test
    public void testSet(){
        sharedJedisSentinelClientImpl.del("set1");
        sharedJedisSentinelClientImpl.sadd("set1", "value1", "value2", "value3");
        Assert.assertEquals(sharedJedisSentinelClientImpl.scard("set1").longValue(), 3);
        Assert.assertEquals(sharedJedisSentinelClientImpl.sismember("set1", "value1").booleanValue(), true);
        sharedJedisSentinelClientImpl.srem("set1", "value1");
        Assert.assertEquals(sharedJedisSentinelClientImpl.scard("set1").longValue(), 2);
        sharedJedisSentinelClientImpl.del("set1");
    }

    @Test
    public void testHash(){
        sharedJedisSentinelClientImpl.del("hsh1");
        sharedJedisSentinelClientImpl.hset("hshKey1", "name", "zhangsan");
        sharedJedisSentinelClientImpl.hset("hshKey1", "sex", "male");
        sharedJedisSentinelClientImpl.hset("hshKey1", "age", "20");
        Assert.assertEquals(sharedJedisSentinelClientImpl.hget("hshKey1", "sex"), "male");

        Map<String, String> map = sharedJedisSentinelClientImpl.hgetAll("hshKey1");
        for(Map.Entry<String, String> entry : map.entrySet()){
            if (entry.getKey().equals("name")){
                Assert.assertEquals(sharedJedisSentinelClientImpl.hget("hshKey1", "name"), entry.getValue());
            }else if (entry.getKey().equals("sex")){
                Assert.assertEquals(sharedJedisSentinelClientImpl.hget("hshKey1", "sex"), entry.getValue());
            }if (entry.getKey().equals("age")){
                Assert.assertEquals(sharedJedisSentinelClientImpl.hget("hshKey1", "age"), entry.getValue());
            }
        }

        sharedJedisSentinelClientImpl.del("hsh1");

    }
}
