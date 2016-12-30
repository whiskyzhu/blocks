package com.blocks.cache.redis;

import com.blocks.cache.redis.exception.RedisClientException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import redis.clients.jedis.ShardedJedisSentinelPool;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractSharededJedisSentinelClient {
	private String configPath;
	protected ShardedJedisSentinelPool shardedJedisSentinelPool;

	protected AbstractSharededJedisSentinelClient(){}

	protected AbstractSharededJedisSentinelClient(String configPath) {
		this.configPath = configPath;
		this.shardedJedisSentinelPool = XMLParser.parse(this.configPath);
	}

	protected AbstractSharededJedisSentinelClient(List<String> masters, Set<String> sentinels) {
		this.shardedJedisSentinelPool = new ShardedJedisSentinelPool(masters, sentinels);
	}

	protected AbstractSharededJedisSentinelClient(GenericObjectPoolConfig poolConfig, List<String> masters, Set<String> sentinels) {
		this.shardedJedisSentinelPool = new ShardedJedisSentinelPool(poolConfig, masters, sentinels);
	}
	
	public ShardedJedisSentinelPool getSharedJedisSentinelPool(){
		return this.shardedJedisSentinelPool;
	}

	private static class XMLParser {
		private static XPath path;
		private static Document doc;

		private static String getString(Object node, String expression) throws XPathExpressionException {
			return ((String) path.evaluate(expression, node, XPathConstants.STRING));
		}

		private static NodeList getList(Object node, String expression) throws XPathExpressionException {
			return ((NodeList) path.evaluate(expression, node, XPathConstants.NODESET));
		}

		private static Node getNode(Object node, String expression) throws XPathExpressionException {
			return ((Node) path.evaluate(expression, node, XPathConstants.NODE));
		}

		/**
		 * 解析配置文件模板：
		 * <config> 
		 *  <!-- 基础参数配置 -->  
		 *	  <timeOut>2000</timeOut>  
		 *	  <!-- 连接池参数配置 -->  
		 *	  <poolConfig> 
		 *	    <maxIdle>50</maxIdle>  
		 *	    <minIdle>10</minIdle> 
		 *	  </poolConfig>  
		 *	  <!-- Sentinel服务器参数配置,以下只是示例-->  
		 *	  <sentinels> 
		 *	    <sentinel> 
		 *	      <ip>10.27.18.228</ip>  
		 *	      <port>26379</port> 
		 *	    </sentinel>  
		 *	    <sentinel> 
		 *	      <ip>10.27.18.227</ip>  
		 *	      <port>26379</port> 
		 *	    </sentinel>  
		 *	    <sentinel> 
		 *	      <ip>10.27.18.225</ip>  
		 *	      <port>26379</port> 
		 *	    </sentinel> 
		 *	  </sentinels>  
		 *	  <!-- 数据分片参数配置 -->  
		 *	  <shards> 
		 *	    <shardName>dmos_dev_master_1</shardName> 
		 *	  </shards>  
		 *	</config>
		 */
		public static ShardedJedisSentinelPool parse(String redisConfig) {
			try {
				DocumentBuilder dbd = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = dbd.parse(XMLParser.class.getResourceAsStream(redisConfig));
				path = XPathFactory.newInstance().newXPath();
				Node rootN = getNode(doc, "config");
				if (null == rootN) {
					throw new RedisClientException("Invalid xml format, can't find <config> root node!");
				}
				String timeOut = getString(rootN, "timeOut");
				if ((null == timeOut) || ("".equals(timeOut.trim()))) {
					timeOut = "2000";
				}
				String password = getString(rootN, "password");
				if ((null == password) || ("".equals(password.trim()))) {
					password = null;
				}
				String dbIndex = getString(rootN, "dbIndex");
				if ((null == dbIndex) || ("".equals(dbIndex.trim()))) {
					dbIndex = "0";
				}
				GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
				Node poolConfigNode = getNode(rootN, "poolConfig");
				if (poolConfigNode != null) {
					poolConfig.setMaxTotal(2147483647);
					poolConfig.setMaxWaitMillis(200L);
					poolConfig.setBlockWhenExhausted(false);
					String maxIdle = getString(poolConfigNode, "maxIdle");
					if ((null != maxIdle) && (!("".equals(maxIdle.trim())))) {
						poolConfig.setMaxIdle(Integer.valueOf(maxIdle).intValue());
					}
					String minIdle = getString(poolConfigNode, "minIdle");
					if ((null != minIdle) && (!("".equals(minIdle.trim())))) {
						poolConfig.setMinIdle(Integer.valueOf(minIdle).intValue());
					}
					String lifo = getString(poolConfigNode, "lifo");
					if ((null != lifo) && (!("".equals(lifo.trim())))) {
						poolConfig.setLifo(Boolean.valueOf(lifo).booleanValue());
					}
					String minEvictableIdleTimeMillis = getString(poolConfigNode, "minEvictableIdleTimeMillis");
					if ((null != minEvictableIdleTimeMillis) && (!("".equals(minEvictableIdleTimeMillis.trim()))))
						poolConfig.setMinEvictableIdleTimeMillis(Long.valueOf(minEvictableIdleTimeMillis).longValue());
					else {
						poolConfig.setMinEvictableIdleTimeMillis(60000L);
					}
					String softMinEvictableIdleTimeMillis = getString(poolConfigNode, "softMinEvictableIdleTimeMillis");
					if ((null != softMinEvictableIdleTimeMillis)
							&& (!("".equals(softMinEvictableIdleTimeMillis.trim())))) {
						poolConfig.setSoftMinEvictableIdleTimeMillis(
								Long.valueOf(softMinEvictableIdleTimeMillis).longValue());
					}
					String numTestsPerEvictionRun = getString(poolConfigNode, "numTestsPerEvictionRun");
					if ((null != numTestsPerEvictionRun) && (!("".equals(numTestsPerEvictionRun.trim()))))
						poolConfig.setNumTestsPerEvictionRun(Integer.valueOf(numTestsPerEvictionRun).intValue());
					else {
						poolConfig.setNumTestsPerEvictionRun(-1);
					}
					String evictionPolicyClassName = getString(poolConfigNode, "evictionPolicyClassName");
					if ((null != evictionPolicyClassName) && (!("".equals(evictionPolicyClassName.trim())))) {
						poolConfig.setEvictionPolicyClassName(evictionPolicyClassName);
					}
					String testOnBorrow = getString(poolConfigNode, "testOnBorrow");
					if ((null != testOnBorrow) && (!("".equals(testOnBorrow.trim())))) {
						poolConfig.setTestOnBorrow(Boolean.valueOf(testOnBorrow).booleanValue());
					}
					String testOnReturn = getString(poolConfigNode, "testOnReturn");
					if ((null != testOnReturn) && (!("".equals(testOnReturn.trim())))) {
						poolConfig.setTestOnReturn(Boolean.valueOf(testOnReturn).booleanValue());
					}
					String testWhileIdle = getString(poolConfigNode, "testWhileIdle");
					if ((null != testWhileIdle) && (!("".equals(testWhileIdle.trim())))) {
						poolConfig.setTestWhileIdle(Boolean.valueOf(testWhileIdle).booleanValue());
					} else
						poolConfig.setTestWhileIdle(true);

					String timeBetweenEvictionRunsMillis = getString(poolConfigNode, "timeBetweenEvictionRunsMillis");
					if ((null != timeBetweenEvictionRunsMillis) && (!("".equals(timeBetweenEvictionRunsMillis.trim()))))
						poolConfig.setTimeBetweenEvictionRunsMillis(
								Long.valueOf(timeBetweenEvictionRunsMillis).longValue());
					else {
						poolConfig.setTimeBetweenEvictionRunsMillis(30000L);
					}
					String jmxEnabled = getString(poolConfigNode, "jmxEnabled");
					if ((null != jmxEnabled) && (!("".equals(jmxEnabled.trim())))) {
						poolConfig.setJmxEnabled(Boolean.valueOf(jmxEnabled).booleanValue());
					}
					String jmxNamePrefix = getString(poolConfigNode, "jmxNamePrefix");
					if ((null != jmxNamePrefix) && (!("".equals(jmxNamePrefix.trim())))) {
						poolConfig.setJmxNamePrefix(jmxNamePrefix);
					}
				}
				Node sentinelNode = getNode(rootN, "sentinels");
				Set<String> sentinels = new HashSet<String>();
				NodeList sentinelConfigs = getList(sentinelNode, "sentinel");
				if ((sentinelConfigs.getLength() != 0) && (sentinelConfigs.getLength() < 2)) {
					throw new RedisClientException("Configuration error,no less than 2 sentinels");
				}
				for (int i = 0; i < sentinelConfigs.getLength(); ++i) {
					Node sentinelConfig = sentinelConfigs.item(i);
					String ip = getString(sentinelConfig, "ip");
					String port = getString(sentinelConfig, "port");
					if ((null == ip) || ("".equals(ip.trim()))) {
						throw new RedisClientException("Configuration error,sentinel host can not be null");
					}
					if ((null == port) || ("".equals(port.trim()))) {
						port = "26379";
					}
					sentinels.add(ip + ":" + port);
				}
				List<String> masters = new ArrayList<String>();
				Node mastersNode = getNode(rootN, "shards");
				if (mastersNode == null) {
					throw new RedisClientException("Configuration error, <shards> can not be null in <shardConfig> ");
				}
				NodeList masterNodes = getList(mastersNode, "shardName");
				if (masterNodes.getLength() == 0) {
					throw new RedisClientException("Configuration error, <shardName> can not be null in <shards> ");
				}
				for (int i = 0; i < masterNodes.getLength(); ++i) {
					String master = masterNodes.item(i).getTextContent();
					if ((null == master) || ("".equals(master.trim()))) {
						throw new RedisClientException("Configuration error,<master> can not be null in <shard>");
					}
					masters.add(master);
				}
				return new ShardedJedisSentinelPool(masters, sentinels, poolConfig, Integer.valueOf(timeOut).intValue(),
						password, Integer.valueOf(dbIndex).intValue());
			} catch (IOException e) {
				throw new RedisClientException("IOException!", e);
			} catch (Exception ex) {
				throw new RedisClientException("Fail to parse redis configure file.", ex);
			}
		}
	}
}
