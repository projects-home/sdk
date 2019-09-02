package com.x.sdk.mds;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.sdk.ccs.util.ConfigTool;

/**
 * @deprecated (2018-04-02, never use, see com.x.sdk.component.mds)
 * @author mayt
 *
 */
@Deprecated
public class KafkaConsumerFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerFactory.class);

    private static Map<String, KafkaConsumer<String, String>> topicToconsumers = new ConcurrentHashMap<String, KafkaConsumer<String, String>>();

    private static Map<String, Object> confMap;

    private KafkaConsumerFactory() {
        // 禁止私有化
    }

    static {
        init();
    }

    private static void init() {
        confMap = ConfigTool.getConfigFile("kafka.properties");
//        confMap.put("bootstrap.servers", "192.168.0.225:9092");
//        confMap.put("group.id", "test22");
//        confMap.put("enable.auto.commit", "true");
//        confMap.put("auto.commit.interval.ms", "1000");
//        confMap.put("max.poll.records", "1");
//        confMap.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        confMap.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        LOGGER.info("kafka.properties load , {}", confMap);
    }

    public static KafkaConsumer<String, String> getMessageConsumer(String topic) {
        KafkaConsumer<String, String> consumer = topicToconsumers.get(topic);
        if (consumer == null) {
            consumer = new KafkaConsumer<String, String>(confMap);
            consumer.subscribe(Arrays.asList(topic));
            topicToconsumers.put(topic, consumer);
        }
        return consumer;
    }

}
