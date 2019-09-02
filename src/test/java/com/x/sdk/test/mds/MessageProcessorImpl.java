package com.x.sdk.test.mds;

import com.alibaba.fastjson.JSONObject;
import com.x.sdk.component.mds.IMessageProcessor;
import com.x.sdk.component.mds.vo.MessageAndMetadata;

public class MessageProcessorImpl implements IMessageProcessor {
    @Override
    public void process(MessageAndMetadata message) throws Exception {

        String msg = new String(message.getMessage(), "UTF-8");
        // topic
        String topic = message.getTopic();
        // 消息格式
        System.out.println("--------------------------------start--------------------------------");
        System.out.println("********** 消息中心开始消费消息。消息内容：" + msg);
        System.out.println("********** 消息中心开始消费消息。topic内容：" + topic);
        System.out.println("********** 消息中心开始消费消息。partitionId：" + message.getPartitionId());
        System.out.println("********** 消息中心开始消费消息。offset：" + message.getOffset());
        System.out.println("---------------------------------end---------------------------------");
    }
}
