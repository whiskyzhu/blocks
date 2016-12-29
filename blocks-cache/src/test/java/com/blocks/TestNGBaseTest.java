package com.blocks;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by lotus on 2016/12/28.
 */
@ContextConfiguration(locations = {"classpath:spring-redis.xml"})
@TransactionConfiguration(defaultRollback = false)
public class TestNGBaseTest extends AbstractTestNGSpringContextTests {
}
