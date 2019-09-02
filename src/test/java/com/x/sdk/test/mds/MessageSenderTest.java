package com.x.sdk.test.mds;

import com.x.sdk.component.mds.IMessageSender;
import com.x.sdk.component.mds.MDSClientFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MessageSenderTest {

	public static void main(String[] args) {
//		Properties props = new Properties();
//		props.put("bootstrap.servers", "172.16.8.27:19092,172.16.8.39:19092,172.16.8.31:19092");
//		props.put("acks", "all");
//		props.put("delivery.timeout.ms", 300000);
//
//		props.put("batch.size", 16384);
//		props.put("linger.ms", 1);
//
//		props.put("buffer.memory", 33554432);
//		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//		Producer<String, String> producer = null;
//
//		try {
//			producer = new KafkaProducer<>(props);
//			for (int i = 0; i < 10; i++)
//				producer.send(new ProducerRecord<String, String>("test", 0, null, "new"+Integer.toString(i)));
//
//			producer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			producer.close();
//		}

		IMessageSender messageSender = MDSClientFactory.getSenderClient("com.x.sdk.test.mds");
		messageSender.send("this is a sdk build msg");
	}

}
