package com.x.sdk.test.component.mds;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.x.sdk.component.mds.IMessageProcessor;
import com.x.sdk.component.mds.vo.MessageAndMetadata;
import com.x.sdk.mds.vo.BusinessMessage;

public class MessageProcessorImpl implements IMessageProcessor{

	@Override
	public void process(MessageAndMetadata message) throws Exception {
		if (null != message) {
			System.out.println("------Topic:" + message.getTopic() +",content:"
					+ new String(message.getMessage(), "UTF-8")
					);
			System.out.println("111");
		}

	}

}
