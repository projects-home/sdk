package com.x.sdk.component.mds.base;

import com.alibaba.fastjson.JSON;
import com.x.sdk.component.mds.constants.MDSSenderConstants;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MdsConfigLoader {

    private static final String PRODUCER_CONF_PATH = "com/x/sdk/mds/sdk-mds-producer.properties";
    private static final String SYS_MDS_PRO_NAME = "sdk-mds-producer.properties";

    private static final String CONSUMER_CONF_PATH = "com/x/sdk/mds/sdk-mds-consumer.properties";
    private static final String SYS_MDS_CON_NAME = "sdk-mds-producer.properties";

    private static Properties producerProp;
    private static Properties consumerProp;

    private final static Lock producerLock = new ReentrantLock();
    private final static Lock consumerLock = new ReentrantLock();

    private static final Logger LOGGER = LoggerFactory.getLogger(MdsConfigLoader.class);

    private static Properties loadProp(String path, String sysProperty){
        InputStream is;
        Properties prop = new Properties();
        String conf = System.getProperty(sysProperty);
        try {
            if (!StringUtil.isBlank(conf)) {
                is = new FileInputStream(conf);
                LOGGER.info("通过配置加载mds默认配置 " + conf);
            } else {
                is = MdsConfigLoader.class.getClassLoader().getResourceAsStream(path);
            }
            prop.load(is);
            LOGGER.info("加载的属性文件prop:" + JSON.toJSONString(prop));
        } catch (IOException e) {
            throw new SDKException("loding config file failed", e);
        }
        return prop;
    }

    public static Properties getProducerProp(){
        if (null == producerProp){
            producerLock.lock();
            try {
                if(null == producerProp){
                    producerProp = loadProp(PRODUCER_CONF_PATH, SYS_MDS_PRO_NAME);
                }
            }catch (Exception e){
                LOGGER.error("load producer prop failed", e);
            }finally {
                producerLock.unlock();
            }
        }
        return producerProp;
    }

    public static Properties getConsumerProp(){
        if(null == consumerProp){
            consumerLock.lock();
            try {
                if(null == consumerProp){
                    consumerProp = loadProp(CONSUMER_CONF_PATH, SYS_MDS_CON_NAME);
                }
            }catch (Exception e){
                LOGGER.error("load consumer prop failed", e);
            }finally {
                consumerLock.unlock();
            }
        }
        return consumerProp;
    }

    public static void main(String[] args) {
        Properties prop = getProducerProp();
        System.out.println(prop.getProperty(MDSSenderConstants.BOOTSTRAP_SERVERS_LIST));
    }
}
