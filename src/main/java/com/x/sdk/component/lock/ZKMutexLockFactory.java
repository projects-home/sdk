package com.x.sdk.component.lock;

import com.x.sdk.component.base.ComponentConfigLoader;
import com.x.sdk.component.ccs.zookeeper.ZKClient;
import com.x.sdk.component.ccs.zookeeper.impl.ZKPoolFactory;
import com.x.sdk.component.lock.zklock.ZKMutexLock;
import com.x.sdk.component.mo.PaasConf;
import com.x.sdk.exception.PaasRuntimeException;
import com.x.sdk.exception.SDKException;

public class ZKMutexLockFactory {

	public static AbstractMutexLock getZKMutexLock(String zkAddr,int zkTimeoutMillSecs,String zkLockNodePath) throws PaasRuntimeException {
		ZKClient zkClient = null;
		try {
			zkClient = ZKPoolFactory.getZKPool(zkAddr,zkTimeoutMillSecs).getZkClient(zkAddr);
			return new ZKMutexLock(zkClient.getInterProcessLock(zkLockNodePath));
		} catch (Exception e) {
			throw new SDKException("获取分布式锁失败", e);
		}		
	}
	public static AbstractMutexLock getZKMutexLock(String zkAddr,String zkLockNodePath) throws PaasRuntimeException{
		//zk连接超时时间默认为60000毫秒，即1分钟。
		return getZKMutexLock(zkAddr,60000,zkLockNodePath);		
	}
	
	public static AbstractMutexLock getZKMutexLock(String zkLockNodePath) throws PaasRuntimeException{
		PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
		return getZKMutexLock(authInfo.getCcsZkAddress(),zkLockNodePath);		
	}	
	
}
