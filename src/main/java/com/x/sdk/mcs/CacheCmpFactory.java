package com.x.sdk.mcs;

import com.x.sdk.mcs.impl.CacheClient;
import com.x.sdk.mcs.impl.CacheClusterClient;
import com.x.sdk.mcs.interfaces.ICacheClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class CacheCmpFactory {

	private static Map<String, ICacheClient> cacheClients = new ConcurrentHashMap<String, ICacheClient>();

	private CacheCmpFactory() {
		// do nothing
	}

	public static ICacheClient getClient(Properties config) {
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxTotal(Integer.parseInt(config.getProperty("mcs.maxtotal", "500")));
		genericObjectPoolConfig.setMaxIdle(Integer.parseInt(config.getProperty("mcs.maxIdle", "10")));
		genericObjectPoolConfig.setMinIdle(Integer.parseInt(config.getProperty("mcs.minIdle", "5")));
		genericObjectPoolConfig.setTestOnBorrow(Boolean.parseBoolean(config.getProperty("mcs.testOnBorrow", "true")));

		String host = config.getProperty("mcs.host", "127.0.0.1:6379");

		String[] hostArray = host.split(";");
		ICacheClient cacheClient = null;
		if (null != cacheClients.get(host)) {
			return cacheClients.get(host);
		}
		if (hostArray.length > 1) {
			cacheClient = new CacheClusterClient(genericObjectPoolConfig, hostArray);
		} else {
			cacheClient = new CacheClient(genericObjectPoolConfig, host);
		}
		cacheClients.put(host, cacheClient);
		return cacheClient;
	}

	public static ICacheClient getClient() throws IOException {
		Properties config = new Properties();
		config.load(CacheCmpFactory.class.getResourceAsStream("/redis.conf"));
		return getClient(config);
	}

	public static ICacheClient getClient(InputStream inputStream) throws IOException {
		Properties config = new Properties();
		config.load(inputStream);
		return getClient(config);
	}
}
