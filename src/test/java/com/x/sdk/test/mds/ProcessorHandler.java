package com.x.sdk.test.mds;

import com.x.sdk.component.mds.IMessageProcessor;
import com.x.sdk.component.mds.IMsgProcessorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangluyang
 * @Date 2019/1/11 11:40 AM
 * @Version 1.0
 */
public class ProcessorHandler implements IMsgProcessorHandler {
    @Override
    public IMessageProcessor[] createInstances(int partitionNum) {
        // TODO Auto-generated method stub
        List<IMessageProcessor> processors = new ArrayList<>();
        IMessageProcessor processor = null;
        for (int i = 0; i < partitionNum; i++) {
            processor = new MessageProcessorImpl();
            processors.add(processor);
        }
        return processors.toArray(new IMessageProcessor[processors.size()]);
    }
}
