package com.x.sdk.component.mds;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.x.sdk.component.mds.impl.sender.MessageSender;
import com.x.sdk.util.Assert;
import com.x.sdk.util.ResourceUtil;

public class MsgSenderCmpFactory {

	static {
		ResourceUtil.addBundle("com.x.sdk.mds.ipaas-message");
	}
  
	private MsgSenderCmpFactory() {
		// 禁止私有化
	}

	private static Map<String, IMessageSender> _senders = new ConcurrentHashMap<String, IMessageSender>();

	public static IMessageSender getClient(Properties kafaProps, String topic) {
		IMessageSender sender = null;
		Assert.notNull(kafaProps,
				ResourceUtil.getMessage("com.x.sdk.mds.msg.cfg_null"));
		Assert.notNull(topic,
				ResourceUtil.getMessage("com.x.sdk.mds.msg.topic_null"));
		if (null != _senders.get(topic)) {
			return _senders.get(topic);
		}
		// 开始构建实例
		int maxProducer = 0;
		if (null != kafaProps.get(MsgConstant.PROP_MAX_PRODUCER)) {
			maxProducer = Integer.parseInt((String) kafaProps
					.get(MsgConstant.PROP_MAX_PRODUCER));
		}
		int pNum = 0;
		if (null != kafaProps.getProperty(MsgConstant.PARTITION_NUM))
			pNum = Integer.parseInt((String) kafaProps
					.get(MsgConstant.PARTITION_NUM));
		sender = new MessageSender(kafaProps, maxProducer, topic, pNum);
		_senders.put(topic, sender);
		return sender;
	}
}
