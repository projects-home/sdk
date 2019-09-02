package com.x.sdk.test.component.mds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.x.sdk.component.mds.IMessageConsumer;
import com.x.sdk.component.mds.IMessageProcessor;
import com.x.sdk.component.mds.IMessageSender;
import com.x.sdk.component.mds.IMsgProcessorHandler;
import com.x.sdk.component.mds.MDSClientFactory;

public class MdsTest {

	@Test
	public void sendMsgTest() {
		String mdsns = "com.x.sdk.test.mds";//
		IMessageSender msgSender = MDSClientFactory.getSenderClient(mdsns);
		// msgSender.getParititions() 方法废弃，以后不再调用
		for (int i = 0; i < 500; i++) {
			// send方法的第二个参数为随机数，依据该随机数均匀往各个片区发送消息
//			msgSender.send("[test-baas-bmc-topic-msg:" + i + "]This is a test message……", new Random(1000).nextLong());
			// System.out.println("sender---[test-baas-bmc-topic-msg:"+i+"]This
			// is a test message……");
		}

	}

	// 消费消息
	@Test
	public void recvMsgTest() {
		String mdsns = "com.x.sdk.test.mds";//
		IMsgProcessorHandler msgProcessorHandler = new IMsgProcessorHandler() {
			@Override
			public IMessageProcessor[] createInstances(int paramInt) {
				List<IMessageProcessor> processors = new ArrayList<>();
				IMessageProcessor processor = null;
				for (int i = 0; i < paramInt; i++) {
					processor = new MessageProcessorImpl();
					processors.add(processor);
				}
				return processors.toArray(new IMessageProcessor[processors.size()]);

			}
		};
		IMessageConsumer msgConsumer = MDSClientFactory.getConsumerClient(mdsns, msgProcessorHandler, "sdk-test-topic");
		msgConsumer.start();
		synchronized (MdsTest.class) {
			while (true) {
				try {
					MdsTest.class.wait();

				} catch (Exception e) {
					System.out.println("MDS 消费错误，具体信息为：" + e.getMessage());
				}
			}
		}

	}

	// 消费消息
	@Test
	public void recvMsgTest2() {
		String mdsns = "com.x.sdk.test.mds2";//
		IMsgProcessorHandler msgProcessorHandler = new IMsgProcessorHandler() {
			@Override
			public IMessageProcessor[] createInstances(int paramInt) {
				List<IMessageProcessor> processors = new ArrayList<>();
				IMessageProcessor processor = null;
				for (int i = 0; i < paramInt; i++) {
					processor = new MessageProcessorImpl();
					processors.add(processor);
				}
				return processors.toArray(new IMessageProcessor[processors.size()]);
			}
		};
		IMessageConsumer msgConsumer = MDSClientFactory.getConsumerClient(mdsns, msgProcessorHandler, "sdk-test-topic");
		msgConsumer.start();
		synchronized (MdsTest.class) {
			while (true) {
				try {
					MdsTest.class.wait();

				} catch (Exception e) {
					System.out.println("MDS 消费错误，具体信息为：" + e.getMessage());
				}
			}
		}

	}
}
