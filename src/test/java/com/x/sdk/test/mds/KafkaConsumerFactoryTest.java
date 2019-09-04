package com.x.sdk.test.mds;

import java.util.ArrayList;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class KafkaConsumerFactoryTest {

//    public static void main(String[] args) throws Exception {
//        KafkaConsumer<String, String> c = KafkaConsumerFactory.getMessageConsumer("topic-dvp-adp-par");
//        c.seekToBeginning(new ArrayList<TopicPartition>());
//        while (true) {
//            ConsumerRecords<String, String> records = c.poll(100L);
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println("11111topic: " + record.topic() + " key: " + record.key()
//                        + " value: " + record.value() + " partition: " + record.partition());
//                Thread.sleep(1 * 1000L);
//            }
////            Thread.sleep(30 * 1000L);
//        }
//    }

}
