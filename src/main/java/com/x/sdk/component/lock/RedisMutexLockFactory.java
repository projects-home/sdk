package com.x.sdk.component.lock;


import com.x.sdk.component.lock.redislock.CacheInterProcessLock;
import com.x.sdk.component.lock.redislock.RedisLockConstants;
import com.x.sdk.component.lock.redislock.RedisMutexLock;
import com.x.sdk.exception.PaasRuntimeException;
import com.x.sdk.exception.SDKException;
import com.x.sdk.component.mcs.MCSClientFactory;
import com.x.sdk.component.mcs.interfaces.ICacheClient;
import com.x.sdk.util.StringUtil;

public class RedisMutexLockFactory {
	
	public static AbstractMutexLock getRedisMutexLock(String namespace,String redisKey) throws PaasRuntimeException {
		if(StringUtil.isBlank(namespace)){
			throw new SDKException("分布式锁对应的缓存命名空间不能为空");
        }
		try {
			ICacheClient cacheClient= MCSClientFactory.getCacheClient(namespace);
			CacheInterProcessLock cacheInterProcessLock=new CacheInterProcessLock(cacheClient);
			return new RedisMutexLock(redisKey, cacheInterProcessLock);
		} catch (Exception e) {
			throw new SDKException("获取分布式锁失败", e);
		}
	}
	
	public static AbstractMutexLock getRedisMutexLock(String redisKey) throws PaasRuntimeException{
		return getRedisMutexLock(RedisLockConstants.REDIS_LOCK_NAMESPACE,redisKey);
	}
}
