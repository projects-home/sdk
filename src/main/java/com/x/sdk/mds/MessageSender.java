package com.x.sdk.mds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.sdk.ccs.util.ConfigTool;

/**
 * @ClassName: MsgSender
 * @Description: 消息发送单元
 * @deprecated (2018-04-02, never use, see com.x.sdk.component.mds)
 * @author mayt
 *
 */
@Deprecated
public class MessageSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
	private static List<Producer<String, String>> producers = new ArrayList<Producer<String, String>>();
	private static int maxIdle = 0;

	private MessageSender() {
		// 禁止私有化
	}

	static {
		init();
	}

	private static void init() {
		Map<String, Object> confMap = ConfigTool.getConfigFile("kafka.properties");

		// Properties props = new Properties();
		// props.put("bootstrap.servers", "192.168.0.225:9092");
		// props.put("acks", "1");
		// props.put("retries", 0);
		// props.put("batch.size", 16384);
		// props.put("linger.ms", 1);
		// props.put("buffer.memory", 33554432);
		// props.put("key.serializer",
		// "org.apache.kafka.common.serialization.StringSerializer");
		// props.put("value.serializer",
		// "org.apache.kafka.common.serialization.StringSerializer");

		maxIdle = Integer.parseInt((String) confMap.get("mds.maxIdle"));
		for (int i = 0; i < maxIdle; i++) {
			Producer<String, String> producer = new KafkaProducer<String, String>(confMap);
			producers.add(producer);
		}
		LOGGER.info("kafka producer create success !");
	}

	public static void send(String topic, String msg) {
		LOGGER.info("send message [{}] to topic [{}]", msg, topic);
		Producer<String, String> producer = producers.get(Math.abs(new Random().nextInt() % maxIdle));
		producer.send(new ProducerRecord<String, String>(topic, msg));
		producer.flush();
	}
	
	public static void send(String topic, String key, String msg) {
		LOGGER.info("send message [{}] to topic [{}]", msg, topic);
		Producer<String, String> producer = producers.get(Math.abs(new Random().nextInt() % maxIdle));
		producer.send(new ProducerRecord<String, String>(topic, key, msg));
		producer.flush();
	}

}
