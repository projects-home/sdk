package com.x.sdk.component.mds.impl.consumer.client;

import com.alibaba.fastjson.JSONObject;
import com.x.sdk.component.mds.IMessageProcessor;
import com.x.sdk.component.mds.constants.MDSConsumerConstants;
import com.x.sdk.component.mds.vo.MessageAndMetadata;
import com.x.sdk.constant.PaaSConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumer implements Runnable, Serializable {

    private static transient final Logger logger = LoggerFactory
            .getLogger(KafkaConsumer.class);
    private static final long serialVersionUID = 1780042755212645597L;

    private final Properties kafkaProps;
    private IMessageProcessor _processor;

    private volatile boolean _stopped = true;
    private volatile boolean _pausing = true;
    private volatile boolean _paused = true;
    private volatile boolean _running = false;

    public KafkaConsumer(Properties kafkaProp, IMessageProcessor processor){
        this.kafkaProps = kafkaProp;
        this._processor = processor;
        this._stopped = true;
        this._running = false;
    }

    @Override
    public void run() {
        open();
        org.apache.kafka.clients.consumer.KafkaConsumer<String, byte[]> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(kafkaProps);
        consumer.subscribe(Arrays.asList(kafkaProps.getProperty(MDSConsumerConstants.MDS_TOPIC)));
        long timeout = Long.valueOf(kafkaProps.getProperty(MDSConsumerConstants.MDS_CONSUMER_TIMEOUT));
        try {
            while (_running && !_stopped){
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(timeout,0));
                if(null!=records){
                    records.forEach((record)->{
                        MessageAndMetadata mmeta = new MessageAndMetadata();
                        mmeta.setTopic(kafkaProps.getProperty(MDSConsumerConstants.MDS_TOPIC));
                        mmeta.setOffset(record.offset());
                        mmeta.setPartitionId(String.valueOf(record.partition()));
                        byte[] payload = record.value();
                        mmeta.setMessage(payload);
                        if (null != record.key()) {
                            byte[] keyload = null;
                            try {
                                keyload = record.key().getBytes(PaaSConstant.CHARSET_UTF8);
                            } catch (UnsupportedEncodingException e) {
                                logger.error("message key convert failure", e);
                            }
                            mmeta.setKey(keyload);
                        }
                        try {
                            _processor.process(mmeta);
                        } catch (Exception e) {
                            throw new FailedFetchException(e);
                        }
                    });

                    //使用异步提交提高消费速度，防止阻塞
                    consumer.commitAsync();
                }
            }
        } catch (Exception e) {
            throw new FailedFetchException(e);
        } finally {
            try {
                consumer.commitSync();      //同步提交防止重平衡重复消费
            } finally {
                consumer.close();
            }
        }
    }

    private void open(){
        _running = true;
        _stopped = false;
    }

    public void stop(){
        _running = false;
        _stopped = true;
    }
}
