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
            IMessageConsumer msgConsumer = MDSClientFactory.getConsumerClient("com.x.sdk.test.mds", new ProcessorHandler());
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
        new ConsumerTestTask().init();
    }
}
