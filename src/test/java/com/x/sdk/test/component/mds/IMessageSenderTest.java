package com.x.sdk.test.component.mds;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.sdk.component.mds.IMessageSender;
import com.x.sdk.component.mds.MDSClientFactory;
import com.x.sdk.component.mds.vo.BusinessMessage;

public class IMessageSenderTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(IMessageSenderTest.class);

	private static final String mdsns = "com.x.sdk.test.mds";

	private static final String msg = "{\"name\": \"张三20180404111\"}";

	private static final long partitionKey = new Random(1000).nextLong();
	
	private static IMessageSender iMessageSender;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		iMessageSender = MDSClientFactory.getSenderClient(mdsns);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		iMessageSender = null;
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testSendStringLong() {
//		iMessageSender.send(msg, partitionKey, "key");
	}

	@Test
	public void testSendByteArrayLong() {
	}

	@Test
	public void testSendStringLongString() {
	}

	@Test
	public void testSendByteArrayLongString() {
	}

	@Test
	public void testGetParititions() {
		LOGGER.info("分片: " + iMessageSender.getParititions());
	}
	
	@Test
	public void testSendBusinessMessage() {
		BusinessMessage businessMessage = new BusinessMessage();
		businessMessage.setData(msg);
//		iMessageSender.send(businessMessage, partitionKey);
	}

}
