package com.x.sdk.test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.x.sdk.util.Md5Encoder;

public class Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) throws UnsupportedEncodingException {
		// Properties originalProps = new Properties();
		// originalProps.put("metadata.broker.list", "192.168.0.225:9092");
		// originalProps.put("serializer.class",
		// "kafka.serializer.DefaultEncoder");
		// originalProps.put("key.serializer.class",
		// "kafka.serializer.StringEncoder");
		//// originalProps.put("partitioner.class",
		// "com.ai.paas.ipaas.mds.impl.sender.ModPartitioner");
		// originalProps.put("request.required.acks", "1");
		// originalProps.put("queue.buffering.max.messages", "1048576");
		// originalProps.put("producer.type", "sync");
		// originalProps.put("message.send.max.retries", "3");
		// originalProps.put("compression.codec", "none");
		// originalProps.put("request.timeout.ms", "20000");
		// originalProps.put("batch.num.messages", "200");
		// originalProps.put("maxProducer", "0");
		// originalProps.put("send.buffer.bytes", "67108864");
		// originalProps.put("mds.topic", "baas_bmc_service_out_queue");
		// Properties props = new Properties();
		// props.put("zk.connect", "192.168.0.226:39181");
		// props.put("serializer.class", "kafka.serializer.StringEncoder");
		// props.put("zk.connectiontimeout.ms", "20000");
		// ProducerConfig config = new ProducerConfig(originalProps);
		// Producer<String, byte[]> producer = new Producer<>(config);
		// String topic = "test";
		// String msg = "mayt-test-msg";
		// KeyedMessage<String, byte[]> km = new KeyedMessage<String, byte[]>(
		// topic, String.valueOf(0),
		// msg.getBytes("utf-8"));
		// producer.send(km);

		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.0.225:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		for (int i = 0; i < 10; i++) {
			producer.send(new ProducerRecord<String, String>("topic_name_test", Integer.toString(i), Integer.toString(i)));
		}
		producer.close();

		LOGGER.error("end");
	}
	
	@org.junit.Test
	public void test() {
	    Map<String, String> map1 = new HashMap<>();
	    Map<String, String> map2 = new HashMap<>();
	    map1.put("a", "1");
	    map1.put("b", "2");
	    map2.put("b", "2");
	    map2.put("a", "1");
	    System.out.println(JSON.toJSONString(map1));
	    System.out.println(JSON.toJSONString(map2));
	    System.out.println(Md5Encoder.encodePassword(JSON.toJSONString(map1)));
	    System.out.println(Md5Encoder.encodePassword(JSON.toJSONString(map2)));
	}

}
