package com.x.sdk.component.mcs;

import com.x.sdk.util.ConfigLoader;
import com.x.sdk.util.ConfigTool;
import com.x.sdk.component.mcs.interfaces.ICacheClient;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class MCSClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(MCSClientFactory.class);
	private static Map<String, ICacheClient> baseMap_serviceMode = new ConcurrentHashMap<String, ICacheClient>();
	private static Map<String, ICacheClient> baseMap_sdkMode = new ConcurrentHashMap<String, ICacheClient>();
	private static Map<String, String> CACHENS_MCS_MAP = new ConcurrentHashMap<>();

    private MCSClientFactory() {

    }

    public static ICacheClient getCacheClient(String cachens) {
//    	PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//    	if(StringUtil.isBlank(authInfo.getPaasSdkMode())||SDKConstants.PAASMODE.PAAS_SERVICE_MODE.equals(authInfo.getPaasSdkMode())){
//    		return getCacheClientByServiceMode(cachens);
//    	}
//    	else{
    		return getCacheClientBySdkMode(cachens);
//    	}
    }

//	private static ICacheClient getCacheClientByServiceMode(String cachens) {
//		if (StringUtil.isBlank(cachens)) {
//            throw new SdkException("请输入缓存服务配置映射的常量标识");
//        }
//        String mcsId = ConfigTool.getMCSId(cachens);
//        String mcsPwd = ConfigTool.getServicePwd(mcsId);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//        AuthDescriptor authDescriptor = new AuthDescriptor(authInfo.getAuthUrl(),
//                authInfo.getPid(), mcsPwd, mcsId);
//        String keyId=authInfo.getPid()+"."+mcsId;
//        ICacheClient client;
//        try {
//        	if (!baseMap_serviceMode.containsKey(keyId)) {
//        		client = CacheFactory.getClient(authDescriptor);
//    			baseMap_serviceMode.put(keyId, client);
//    		}
//        	else{
//        		client=baseMap_serviceMode.get(keyId);
//        	}
//        } catch (Exception e) {
//            throw new SdkException("无法获取缓存服务[" + mcsId + "]对应的客户端实例", e);
//        }
//        return client;
//	}client
	private static ICacheClient getCacheClientBySdkMode(String cachens) {
		if (StringUtil.isBlank(cachens)) {
			throw new SDKException("请输入缓存服务配置映射的常量标识");
		}
		String mcsId;
		if (CACHENS_MCS_MAP.containsKey(cachens)) {
			mcsId = CACHENS_MCS_MAP.get(cachens);
		} else {
			mcsId = ConfigTool.getMCSId(cachens);
			CACHENS_MCS_MAP.put(cachens, mcsId);
		}
//		PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
		String appname = ConfigLoader.getApp();
		Properties redisProp=ConfigTool.assembleMcsProperties(cachens);
//		LOG.debug("authInfo="+JSON.toJSONString(authInfo));
		
		String keyId=appname+"."+mcsId;
		ICacheClient client;
		try {
			if (!baseMap_sdkMode.containsKey(keyId)) {
				client = CacheCmpFactory.getClient(redisProp);
				baseMap_sdkMode.put(keyId, client);
			}
			else{
				client=baseMap_sdkMode.get(keyId);
			}
		} catch (Exception e) {
			throw new SDKException("无法获取缓存服务[" + mcsId + "]对应的客户端实例", e);
		}
		return client;
	}

}
