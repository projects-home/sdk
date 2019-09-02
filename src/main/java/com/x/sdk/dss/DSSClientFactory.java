package com.x.sdk.dss;

import com.alibaba.fastjson.JSON;
import com.x.sdk.util.ConfigLoader;
import com.x.sdk.util.ConfigTool;
import com.x.sdk.dss.interfaces.IDSSClient;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class DSSClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DSSClientFactory.class);
	private static Map<String, IDSSClient> baseMap_serviceMode = new ConcurrentHashMap<String, IDSSClient>();
	private static Map<String, IDSSClient> baseMap_sdkMode = new ConcurrentHashMap<String, IDSSClient>();

    private DSSClientFactory() {

    }

    public static IDSSClient getDSSClient(String dssns) {
//    	PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//    	if(StringUtil.isBlank(authInfo.getPaasSdkMode())||SDKConstants.PAASMODE.PAAS_SERVICE_MODE.equals(authInfo.getPaasSdkMode())){
//    		return getDssClientByServiceMode(dssns);
//    	}
//    	else{
    		return getDssClientBySdkMode(dssns);
//    	}
    }

    private static IDSSClient getDssClientBySdkMode(String dssns) {
		if (StringUtil.isBlank(dssns)) {
            throw new SDKException("请输入文档存储服务配置映射的常量标识");
        }
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
        String appname = ConfigLoader.getApp();
//		LOG.debug("authInfo="+JSON.toJSONString(authInfo));
		
        String dssId = ConfigTool.getDSSId(dssns);
        Properties dssProp=ConfigTool.assembleDssProperties(dssns);
        String mongoJson=JSON.toJSONString(dssProp);
        String keyId=appname+"."+dssId;
        IDSSClient client;
        try {
        	if (!baseMap_sdkMode.containsKey(keyId)) {
        		client = DSSBaseFactory.getClient(mongoJson);
        		baseMap_sdkMode.put(keyId, client);
    		}
        	else{
        		client=baseMap_sdkMode.get(keyId);
        	}
        } catch (Exception e) {
            throw new SDKException("无法获取文档存储服务[" + dssId + "]对应的客户端实例", e);
        }
        return client;
	}
//	private static IDSSClient getDssClientByServiceMode(String dssns) {
//		if (StringUtil.isBlank(dssns)) {
//            throw new SdkException("请输入文档存储服务配置映射的常量标识");
//        }
//        String dssId = ConfigTool.getDSSId(dssns);
//        String dssPwd = ConfigTool.getServicePwd(dssId);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//        AuthDescriptor authDescriptor = new AuthDescriptor(authInfo.getAuthUrl(),
//                authInfo.getPid(), dssPwd, dssId);
//        String keyId=authInfo.getPid()+"."+dssId;
//        IDSSClient client;
//        try {
//        	if (!baseMap_serviceMode.containsKey(keyId)) {
//        		client = DSSFactory.getClient(authDescriptor);
//    			baseMap_serviceMode.put(keyId, client);
//    		}
//        	else{
//        		client=baseMap_serviceMode.get(keyId);
//        	}
//        } catch (Exception e) {
//            throw new SdkException("无法获取文档存储服务[" + dssId + "]对应的客户端实例", e);
//        }
//        return client;
//	}

}
