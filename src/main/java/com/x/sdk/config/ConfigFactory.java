package com.x.sdk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.x.sdk.constant.CcsConstant;
import com.x.sdk.model.ConfigModel;

public final class ConfigFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);

	private static final String PATH = "config/config.xml";

	private static ApplicationContext appContext;

	private static ConfigModel configModel = new ConfigModel();

//	static {
//		initApplicationContext();
//		read();
//	}

	@SuppressWarnings("unused")
    private static void initApplicationContext() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { PATH });
		context.start();
		appContext = context;
	}

	@SuppressWarnings("unused")
    private static void read() {
		InputStream inputStream = ConfigFactory.class.getClassLoader().getResourceAsStream("disconf.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			LOGGER.error("read disconf.properties error", e);
		}
		configModel = new ConfigModel();
		configModel.setApp(properties.getProperty(CcsConstant.APP));
		configModel.setConfServerHost(properties.getProperty(CcsConstant.CONF_SERVER_HOST));
		configModel.setConfServerUrlRetrySleepSeconds(
				properties.getProperty(CcsConstant.CONF_SERVER_URL_RETRY_SLEEP_SECONDS));
		configModel.setConfServerUrlRetryTimes(properties.getProperty(CcsConstant.CONF_SERVER_URL_RETRY_TIMES));
		configModel.setDebug(Boolean.parseBoolean(properties.getProperty(CcsConstant.DEBUG)));
		configModel.setEnableRemoteConf(Boolean.parseBoolean(properties.getProperty(CcsConstant.ENABLE_REMOTE_CONF)));
		configModel.setEnv(properties.getProperty(CcsConstant.ENV));
		configModel.setIgnore(properties.getProperty(CcsConstant.IGNORE));
		configModel.setVersion(properties.getProperty(CcsConstant.VERSION));
	}

	public static Object getConfig(Class<?> clazz) {
		return appContext.getBean(clazz);
	}

	public static ConfigModel getConfigModel() {
		return configModel;
	}

}
