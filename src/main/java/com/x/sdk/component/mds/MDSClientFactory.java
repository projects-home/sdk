package com.x.sdk.component.mds;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.x.sdk.util.ConfigLoader;
import com.x.sdk.util.ConfigTool;
import com.x.sdk.component.mds.constants.MDSConsumerConstants;
import com.x.sdk.component.mds.constants.MDSSenderConstants;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;

public final class MDSClientFactory {
	private static Map<String, IMessageSender> sendMap_sdkMode = new ConcurrentHashMap<String, IMessageSender>();
	private static Map<String, IMessageConsumer> recvMap_sdkMode = new ConcurrentHashMap<String, IMessageConsumer>();

    private MDSClientFactory() {

    }

    public static IMessageSender getSenderClient(String mdsns) {
		return getSenderClientBySdkMode(mdsns);
    }
    public static IMessageConsumer getConsumerClient(String mdsns, IMsgProcessorHandler msgProcessorHandler){
		return getConsumerClient(mdsns, msgProcessorHandler,null);
    }
    public static IMessageConsumer getConsumerClient(String mdsns, IMsgProcessorHandler msgProcessorHandler,String consumerId){
		return getConsumerClientBySdkMode(mdsns, msgProcessorHandler,consumerId);
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
        String appname = ConfigLoader.getApp();
		Properties kafkaConsumerProp=ConfigTool.assembleMdsConsumerProperties(mdsns);
		String topicId=kafkaConsumerProp.getProperty(MDSConsumerConstants.MDS_TOPIC);
        String keyId=appname+"."+mdsId+"."+consumerId;
		
		IMessageConsumer client;
		try {
			if (!recvMap_sdkMode.containsKey(keyId)) {
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
    
	private static IMessageSender getSenderClientBySdkMode(String mdsns) {
		if (StringUtil.isBlank(mdsns)) {
            throw new SDKException("请输入消息服务配置映射的常量标识");
        }
        String mdsId = ConfigTool.getMDSId(mdsns);
        String appname = ConfigLoader.getApp();
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
