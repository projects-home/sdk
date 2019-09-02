package com.x.sdk.component.base;


import com.alibaba.fastjson.JSON;
import com.x.sdk.component.mo.PaasConf;
import com.x.sdk.constant.SDKConstants;
import com.x.sdk.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 平台技术组件配置加载器<br>
 * Date: 2016年5月5日 <br>
 * Copyright (c) 2016  <br>
 * 
 * @author gucl
 */
public final class ComponentConfigLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ComponentConfigLoader.class);

    private static ComponentConfigLoader INSTANCE = null;

    private static Properties prop;
	static{
		INSTANCE=new ComponentConfigLoader();
		InputStream is = ComponentConfigLoader.class.getClassLoader().getResourceAsStream(SDKConstants.PAAS_CONFIG_FILE);
        LOG.info("【加载的属性文件流】:"+JSON.toJSONString(is));
        try {
            prop = new Properties();
            prop.load(is);
            LOG.info("【加载的属性文件prop】:"+JSON.toJSONString(prop));
        } catch (IOException e) {
            throw new SDKException("loding paas config file failed", e);
        }
	}
    private ComponentConfigLoader() {
        // 禁止实例化
    }

    /**
     * 获取配置加载器单例实例，确保多线程并发情况下高效读取，避免INSTANCE引用指向不同的实例对象
     * 
     * @return
     * 
     */
    public static ComponentConfigLoader getInstance() {
        return INSTANCE;
    }
    public static ComponentConfigLoader loadPaaSConf(Properties p) {
    	LOG.debug("【loadPaaSConf INSTANCE  开始。。。】");
    	  if (INSTANCE == null || prop==null) {
              // 多线程并发获取实例时候，避免等线程锁造成性能低下，因此在创建实例时候进行同步处理
    		  LOG.debug("【loadPaaSConf INSTANCE和prop均为空，ComponentConfigLoader需进行实例化】");
              synchronized (ComponentConfigLoader.class) {
                  if (INSTANCE == null) {
                	  LOG.debug("【loadPaaSConf 线程锁synchronized内，INSTANCE仍为空，ComponentConfigLoader需进行实例化】");
                      INSTANCE = new ComponentConfigLoader();
                      LOG.debug("【loadPaaSConf ComponentConfigLoader实例化结束】");
                      INSTANCE.loadProp(p);
                      LOG.debug("【loadPaaSConf INSTANCE加载prop结束】");
                  }
              }
          }
    	  LOG.debug("【loadPaaSConf INSTANCE  结束。。。】:"+JSON.toJSONString(INSTANCE));
          return INSTANCE;
    	
    }

    private void loadProp(Properties p) {
    	prop=p;
    }

    public PaasConf getPaasAuthInfo() {
        String authUrl = prop.getProperty("paas.auth.url");
        String pid = prop.getProperty("paas.auth.pid");
        String ccsServiceId = prop.getProperty("paas.ccs.serviceid");
        String ccsServicePwd = prop.getProperty("paas.ccs.servicepassword");
        String paasSdkMode = prop.getProperty("paas.sdk.mode");
        String ccsAppName = prop.getProperty("ccs.appname");
        String zkAddress = prop.getProperty("ccs.zk_address");
        String app = prop.getProperty("app");
        /*if (StringUtil.isBlank(authUrl)) {
            throw new SDKException("paas auth url is null");
        }
        if (StringUtil.isBlank(pid)) {
            throw new SDKException("paas auth pid is null");
        }
        if (StringUtil.isBlank(ccsServiceId)) {
            throw new SDKException("paas ccs serviceid is null");
        }
        if (StringUtil.isBlank(ccsServicePwd)) {
            throw new SDKException("paas ccs service password is null");
        }*/
        PaasConf pc = new PaasConf();
        pc.setAuthUrl(authUrl);
        pc.setPid(pid);
        pc.setCcsServiceId(ccsServiceId);
        pc.setCcsPassword(ccsServicePwd);
        pc.setPaasSdkMode(paasSdkMode);
        pc.setCcsAppName(ccsAppName);
        pc.setCcsZkAddress(zkAddress);
        pc.setApp(app);
        return pc;
    }

}
