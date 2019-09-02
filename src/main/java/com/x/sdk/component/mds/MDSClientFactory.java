package com.x.sdk.component.mds;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.x.sdk.ccs.base.ConfigLoader;
import com.x.sdk.ccs.util.ConfigTool;
import com.x.sdk.component.mds.constants.MDSConsumerConstants;
import com.x.sdk.component.mds.constants.MDSSenderConstants;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;

public final class MDSClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(MDSClientFactory.class);
	private static Map<String, IMessageSender> sendMap_serviceMode = new ConcurrentHashMap<String, IMessageSender>();
	private static Map<String, IMessageConsumer> recvMap_serviceMode = new ConcurrentHashMap<String, IMessageConsumer>();
	private static Map<String, IMessageSender> sendMap_sdkMode = new ConcurrentHashMap<String, IMessageSender>();
	private static Map<String, IMessageConsumer> recvMap_sdkMode = new ConcurrentHashMap<String, IMessageConsumer>();

    private MDSClientFactory() {

    }

    public static IMessageSender getSenderClient(String mdsns) {
//    	PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//    	if(StringUtil.isBlank(authInfo.getPaasSdkMode())|| SDKConstants.PAASMODE.PAAS_SERVICE_MODE.equals(authInfo.getPaasSdkMode())){
//    		return getSenderClientByServiceMode(mdsns);
//    	}
//    	else{
    		return getSenderClientBySdkMode(mdsns);
//    	}
    }
    public static IMessageConsumer getConsumerClient(String mdsns, IMsgProcessorHandler msgProcessorHandler){
    		return getConsumerClient(mdsns, msgProcessorHandler,null);
    }
    public static IMessageConsumer getConsumerClient(String mdsns, IMsgProcessorHandler msgProcessorHandler,String consumerId){
//		PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//		if(StringUtil.isBlank(authInfo.getPaasSdkMode())||SDKConstants.PAASMODE.PAAS_SERVICE_MODE.equals(authInfo.getPaasSdkMode())){
//			return getConsumerClientByServiceMode(mdsns, msgProcessorHandler,consumerId);
//		}
//		else{
			return getConsumerClientBySdkMode(mdsns, msgProcessorHandler,consumerId);
//		}
    		
    }
    
    private static IMessageConsumer getConsumerClientBySdkMode(String mdsns, IMsgProcessorHandler msgProcessorHandler,
			String consumerId) {
    	if(StringUtil.isBlank(consumerId)){
    		consumerId="consumer";
		}
    	if (StringUtil.isBlank(mdsns)) {
			throw new SDKException("请输入消息服务配置映射的常量标识");
		}
		String mdsId = ConfigTool.getMDSId(mdsns);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
        String appname = ConfigLoader.getApp();
//		LOG.debug("authInfo="+JSON.toJSONString(authInfo));
		Properties kafkaConsumerProp=ConfigTool.assembleMdsConsumerProperties(mdsns);
		String topicId=kafkaConsumerProp.getProperty(MDSConsumerConstants.MDS_TOPIC);
        String keyId=appname+"."+mdsId+"."+consumerId;
		
		IMessageConsumer client;
		try {
			if (!recvMap_sdkMode.containsKey(keyId)) {
//				kafkaConsumerProp.put(MDSConsumerConstants.KAFKA_CONSUMER_ID, consumerId);
//
//				String mdsConsumerBasePath=kafkaConsumerProp.getProperty(MDSConsumerConstants.MDS_CONSUMER_BASE_PATH);
//				String newMdsConsumerBasePath=mdsConsumerBasePath+"/"+consumerId;
//
//				kafkaConsumerProp.put(MDSConsumerConstants.KAFKA_CONSUMER_ID, consumerId);
//				kafkaConsumerProp.put(MDSConsumerConstants.MDS_PARTITION_RUNNINGLOCK_PATH, newMdsConsumerBasePath+ "/partitions/running");
//				kafkaConsumerProp.put(MDSConsumerConstants.MDS_PARTITION_PAUSELOCK_PATH, newMdsConsumerBasePath+ "/partitions/pause");
//				kafkaConsumerProp.put(MDSConsumerConstants.MDS_PARTITION_OFFSET_BASEPATH, newMdsConsumerBasePath+ "/offsets");
				
				client = MsgConsumerCmpFactory.getClient(kafkaConsumerProp,topicId, msgProcessorHandler);
				recvMap_sdkMode.put(keyId, client);
			}
			else{
				client=recvMap_sdkMode.get(keyId);
			}
		} catch (Exception e) {
			throw new SDKException("无法获取消息服务[" + mdsId + "]对应的客户端实例", e);
		}
		return client;
	}

//	private static IMessageConsumer getConsumerClientByServiceMode(String mdsns,
//			IMsgProcessorHandler msgProcessorHandler, String consumerId) {
//		if (StringUtil.isBlank(mdsns)) {
//            throw new SDKException("请输入消息服务配置映射的常量标识");
//        }
//        String mdsId = ConfigTool.getMDSId(mdsns);
//        String mdsPwd = ConfigTool.getServicePwd(mdsId);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//        AuthDescriptor authDescriptor = new AuthDescriptor(authInfo.getAuthUrl(),
//                authInfo.getPid(), mdsPwd, mdsId);
//        String keyId=authInfo.getPid()+"."+mdsId;
//        if(!StringUtil.isBlank(consumerId)){
//        	keyId+="."+consumerId;
//        }
//        else{
//        	keyId+="."+"consumer";
//        }
//        IMessageConsumer client;
//        try {
//        	if (!recvMap_serviceMode.containsKey(keyId)) {
//        		if(!StringUtil.isBlank(consumerId)){
//        			client = MsgConsumerFactory.getClient(authDescriptor, msgProcessorHandler,consumerId);
//        		}
//        		else{
//        			client = MsgConsumerFactory.getClient(authDescriptor, msgProcessorHandler);
//        		}
//        		recvMap_serviceMode.put(keyId, client);
//    		}
//        	else{
//        		client=recvMap_serviceMode.get(keyId);
//        	}
//        } catch (Exception e) {
//            throw new SDKException("无法获取消息服务[" + mdsId + "]对应的客户端实例", e);
//        }
//        return client;
//	}

//	private static IMessageSender getSenderClientByServiceMode(String mdsns) {
//		if (StringUtil.isBlank(mdsns)) {
//            throw new SDKException("请输入消息服务配置映射的常量标识");
//        }
//        String mdsId = ConfigTool.getMDSId(mdsns);
//        String mdsPwd = ConfigTool.getServicePwd(mdsId);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
//        AuthDescriptor authDescriptor = new AuthDescriptor(authInfo.getAuthUrl(),
//                authInfo.getPid(), mdsPwd, mdsId);
//        String keyId=authInfo.getPid()+"."+mdsId;
//        IMessageSender client;
//        try {
//        	if (!sendMap_serviceMode.containsKey(keyId)) {
//        		client = MsgSenderFactory.getClient(authDescriptor);
//    			sendMap_serviceMode.put(keyId, client);
//    		}
//        	else{
//        		client=sendMap_serviceMode.get(keyId);
//        	}
//        } catch (Exception e) {
//            throw new SDKException("无法获取消息服务[" + mdsId + "]对应的客户端实例", e);
//        }
//        return client;
//	}
    
	private static IMessageSender getSenderClientBySdkMode(String mdsns) {
		if (StringUtil.isBlank(mdsns)) {
            throw new SDKException("请输入消息服务配置映射的常量标识");
        }
        String mdsId = ConfigTool.getMDSId(mdsns);
//        PaasConf authInfo = ComponentConfigLoader.getInstance().getPaasAuthInfo();
        String appname = ConfigLoader.getApp();
//		LOG.debug("authInfo="+JSON.toJSONString(authInfo));
		Properties kafkaSenderProp=ConfigTool.assembleMdsSenderProperties(mdsns);
		String topicId=kafkaSenderProp.getProperty(MDSSenderConstants.MDS_TOPIC);
        String keyId=appname+"."+mdsId;
        IMessageSender client;
        try {
        	if (!sendMap_sdkMode.containsKey(keyId)) {
        		client = MsgSenderCmpFactory.getClient(kafkaSenderProp,topicId);
        		sendMap_sdkMode.put(keyId, client);
    		}
        	else{
        		client=sendMap_sdkMode.get(keyId);
        	}
        } catch (Exception e) {
            throw new SDKException("无法获取消息服务[" + mdsId + "]对应的客户端实例", e);
        }
        return client;
	}

}
