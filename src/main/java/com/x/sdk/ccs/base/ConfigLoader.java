package com.x.sdk.ccs.base;

import com.alibaba.fastjson.JSON;
import com.x.sdk.constant.CcsConstant;
import com.x.sdk.exception.SDKException;
import com.x.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 存放配置中心的连接配置，主机、应用、版本等
 * 
 * @author mayt
 *
 */
public final class ConfigLoader {
    private static final String CONF_PATH = "paas/paas-conf.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

    private static Properties prop;

    private static void loadProp() {
        String conf = System.getProperty("paas-conf.conf");
        InputStream is;
        try {
            if (!StringUtil.isBlank(conf)) {
                is = new FileInputStream(conf);
                LOGGER.info("通过配置加载 " + conf);
            } else {
                is = ConfigLoader.class.getClassLoader().getResourceAsStream(CONF_PATH);
            }

            prop = new Properties();
            prop.load(is);
            LOGGER.info("加载的属性文件prop:" + JSON.toJSONString(prop));
        } catch (IOException e) {
            throw new SDKException("loding config file failed", e);
        }
    }

    public static void main(String[] args) {
        String a = ConfigLoader.getConfServerHost();
        System.out.println(a);
    }

    private ConfigLoader() {
        // 禁止实例化
    }

    public static void loadConf(Properties p) {
        LOGGER.debug("[loadConf   开始。。。]");
        if (prop == null) {
            // 多线程并发获取实例时候，避免等线程锁造成性能低下，因此在创建实例时候进行同步处理
            LOGGER.debug("[prop为空]");
            synchronized (ConfigLoader.class) {
                if (prop == null) {
                    loadProp(p);
                }
            }
        }
        LOGGER.debug("[loadConf   结束。。。]:" + JSON.toJSONString(prop));
    }

    private static void loadProp(Properties p) {
        prop = p;
    }

    private static void init() {
        if (null == prop) {
            // 多线程并发获取实例时候，避免等线程锁造成性能低下，因此在创建实例时候进行同步处理
            LOGGER.debug("[prop为空]");
            synchronized (ConfigLoader.class) {
                if (prop == null) {
                    loadProp();
                }
            }
        }
        LOGGER.debug("[loadConf   结束。。。]:" + JSON.toJSONString(prop));
    }

    public static String getConfServerHost() {
        if (null == prop) {
            init();
        }
        return prop.getProperty(CcsConstant.CONF_SERVER_HOST);
    }

    public static String getApp() {
        if (null == prop) {
            init();
        }
        return prop.getProperty(CcsConstant.APP);
    }

    public static String getVersion() {
        if (null == prop) {
            init();
        }
        return prop.getProperty(CcsConstant.VERSION);
    }

    public static String getEnv() {
        if (null == prop) {
            init();
        }
        return prop.getProperty(CcsConstant.ENV);
    }

}
