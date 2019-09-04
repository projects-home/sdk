package com.x.sdk.test.mds;

import com.x.sdk.component.mds.IMessageConsumer;
import com.x.sdk.component.mds.MDSClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ConsumerTestTask {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerTestTask.class);

    public ConsumerTestTask() {
        init();
    }
    public void init() {
        //kafka启动
        MainThread te = new MainThread();
        te.start();
    }

    // 内部类
    private class MainThread extends Thread {
        public MainThread() {
        }
        public void run() {
            IMessageConsumer msgConsumer = MDSClientFactory.getConsumerClient("com.x.sdk.test.consumer.mds", new ProcessorHandler());
            msgConsumer.start();
            synchronized (ConsumerTestTask.class) {
                while (true) {
                    try {
                        ConsumerTestTask.class.wait();
                    } catch (Exception e) {
                        LOG.error(">>>>>>>>> MDS 消费错误，具体信息为：" + e.getMessage());
                    }
                }
            }

        }
    }

    public static void main(String[] args) {

//        {
//            "MDS001":{
//            "bootstrap.servers":"10.211.55.4:9092",
//                    "key.deserializer":"org.apache.kafka.common.serialization.StringDeserializer",
//                    "value.deserializer":"org.apache.kafka.common.serialization.ByteArrayDeserializer",
//                    "group.id":"test",
//                    "enable.auto.commit":"false",
//                    "auto.commit.interval.ms":"1000",
//                    "fetch.min.bytes":"1",
//                    "fetch.max.wait.ms":"500",
//                    "max.partition.fetch.bytes":"1048576",
//                    "session.timeout.ms":"10000",
//                    "auto.offset.reset":"latest",
//                    "partition.assignment.strategy":"org.apache.kafka.clients.consumer.RoundRobinAssignor",
//                    "max.poll.records":"500",
//                    "mds.consumer.timeout":"100",
//                    "mds.topic":"test"
//        }
//        }

        new ConsumerTestTask().init();
    }
}
