package com.x.sdk.ses;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.x.sdk.ccs.base.ConfigLoader;
import com.x.sdk.ccs.util.ConfigTool;
import com.x.sdk.exception.SDKException;
import com.x.sdk.ses.constants.SESConsants;
import com.x.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class SESClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(SESClientFactory.class);
    private static Map<String, ISearchClient> baseMap = new ConcurrentHashMap<String, ISearchClient>();
    private static Map<String, ISearchClient> baseMap_sdkMode = new ConcurrentHashMap<String, ISearchClient>();

    private SESClientFactory() {

    }

    public static ISearchClient getSearchClient(String sesns) {
//    	PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//    	if(StringUtil.isBlank(authInfo.getPaasSdkMode())||SDKConstants.PAASMODE.PAAS_SERVICE_MODE.equals(authInfo.getPaasSdkMode())){
//    		return getSearchClientByServiceMode(sesns);
//    	}
//    	else{
    		return getSearchClientBySdkMode(sesns);
//    	}
    }

    private static ISearchClient getSearchClientBySdkMode(String sesns) {
    	if (StringUtil.isBlank(sesns)) {
    		throw new SDKException("请输入搜索服务配置映射的常量标识");
    	}
//    	PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
        String appname = ConfigLoader.getApp();
//		LOG.debug("authInfo="+JSON.toJSONString(authInfo));
		
    	String sesId = ConfigTool.getSESId(sesns);
    	Properties sesProp=ConfigTool.assembleSesProperties(sesns);
    	//System.out.println("sesProp="+JSON.toJSONString(sesProp));
    	String eshosts=sesProp.getProperty(SESConsants.ESHOSTS);
    	String indexName=sesProp.getProperty(SESConsants.INDEXNAME);
    	String mappingid=sesProp.getProperty(SESConsants.MAPPINGID);
    	JSONObject mappingJson=(JSONObject) sesProp.get(SESConsants.MAPPING);
    	String mapping=JSON.toJSONString(mappingJson);
    	String shards=sesProp.getProperty(SESConsants.SHARDS);
    	String replicas=sesProp.getProperty(SESConsants.REPLICAS);
    	
    	String keyId = appname + "." + sesId;
    	ISearchClient client;
    	try {
    		if (!baseMap_sdkMode.containsKey(keyId)) {
    			client = SearchCmpClientFactory.getSearchClient(eshosts,indexName,mappingid);
    			baseMap_sdkMode.put(keyId, client);
    			
    			//判断是否存在索引，若不存在，则创建
    			if(!client.existIndex(indexName)){
    				client.createIndex(indexName, Integer.parseInt(shards), Integer.parseInt(replicas));
    			}
    			// addmapping   需加一个判断，盘点mapping是否存在
    			if(!client.existMapping(indexName, mapping)){
    				client.addMapping(indexName, indexName, mapping);
    			}
    		} else {
    			//获取客户端
    			client = baseMap_sdkMode.get(keyId);
    			
    		}
    	} catch (Exception e) {
    		throw new SDKException("无法获取SES服务[" + sesId + "]对应的客户端实例", e);
    	}
    	return client;
    }
//	private static ISearchClient getSearchClientByServiceMode(String sesns) {
//		if (StringUtil.isBlank(sesns)) {
//            throw new SDKException("请输入搜索服务配置映射的常量标识");
//        }
//        String sesId = ConfigTool.getSESId(sesns);
//        String sesPwd = ConfigTool.getServicePwd(sesId);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//        AuthDescriptor authDescriptor = new AuthDescriptor(authInfo.getAuthUrl(),
//                authInfo.getPid(), sesPwd, sesId);
//        String keyId=authInfo.getPid()+"."+sesId;
//        ISearchClient client;
//        try {
//            if (!baseMap.containsKey(keyId)) {
//                client = SearchClientFactory.getSearchClient(authDescriptor);
//                baseMap.put(keyId, client);
//            } else {
//                client = baseMap.get(keyId);
//            }
//        } catch (Exception e) {
//            throw new SDKException("无法获取SES服务[" + sesId + "]对应的客户端实例", e);
//        }
//        return client;
//	}
}
