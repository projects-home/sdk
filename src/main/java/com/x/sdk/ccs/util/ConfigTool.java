package com.x.sdk.ccs.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.x.sdk.ccs.base.ConfigLoader;
import com.x.sdk.component.ccs.CCSClientFactory;
import com.x.sdk.component.ccs.IConfigClient;
import com.x.sdk.component.ccs.constants.ConfigException;
import com.x.sdk.component.idps.constants.IDPSConsants;
import com.x.sdk.component.mds.base.MdsConfigLoader;
import com.x.sdk.component.mds.constants.MDSConsumerConstants;
import com.x.sdk.component.mds.constants.MDSSenderConstants;
import com.x.sdk.config.ConfigFactory;
import com.x.sdk.config.dubbo.DubboConfig;
import com.x.sdk.config.hdfs.HdfsConfig;
import com.x.sdk.config.jdbc.JdbcConfig;
import com.x.sdk.config.sso.SsoConfig;
import com.x.sdk.constant.CcsConstant;
import com.x.sdk.constant.JdbcConstant;
import com.x.sdk.constant.SDKConstants;
import com.x.sdk.dss.constants.DSSConsants;
import com.x.sdk.exception.SDKException;
import com.x.sdk.mcs.constants.MCSConstants;
import com.x.sdk.ses.constants.SESConsants;
import com.x.sdk.util.HttpUtil;
import com.x.sdk.util.StringUtil;
import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTool.class);

    private ConfigTool() {
    }
    
    public static Properties assembleMdsSenderProperties(String namespace) {
		Properties mdsSenderProperties=new Properties();
//		IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
//        if (configClient == null) {
//            throw new SDKException("cann't get sdkmode mds conf because IConfigClient is null");
//        }
        // 获取mds namespace映射信息
        String mdsNSConfStr="";
		try {
			mdsNSConfStr = getConfigItem(
			        SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH);
		} catch (ConfigException e) {
			LOGGER.error("error！cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]",e);
			throw new SDKException("error！cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]");
		}
        if (StringUtil.isBlank(mdsNSConfStr)) {
            throw new SDKException("cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject mdsNSJson = JSONObject.parseObject(mdsNSConfStr);
        //namespace对应的redis服务标识，如MDS001，MDS002
		String mdsId=mdsNSJson.getString(namespace);
		if(StringUtil.isBlank(mdsId)){
			throw new SDKException("cann't get sdkmoe mdsId of namespace["
                    + namespace + "]");
		}
		// 获取kafka集群配置信息
		String kafkaConfStr="";
		try {
			kafkaConfStr = getConfigItem(SDKConstants.SDK_MODE_PAAS_MDS_SENDER_MAPPED_PATH);
		} catch (ConfigException e) {
			LOGGER.error("error！cann't get sdkmode mds sender kafka info from path["
                    + SDKConstants.SDK_MODE_PAAS_MDS_SENDER_MAPPED_PATH + "]",e);
			throw new SDKException("error！cann't get sdkmode mds sender kafka info from path["
                    + SDKConstants.SDK_MODE_PAAS_MDS_SENDER_MAPPED_PATH + "]");
		}
		
		if(StringUtil.isBlank(kafkaConfStr)){
			throw new SDKException("cann't get sdkmode mds sender kafka conf of namespace["
                    + namespace + "],mdsId["+mdsId+"]");
		}
		
		JSONObject kafkaConfJson = JSONObject.parseObject(kafkaConfStr);
		JSONObject kafkaJson=(JSONObject) kafkaConfJson.get(mdsId);

        mdsSenderProperties.put(MDSSenderConstants.BOOTSTRAP_SERVERS_LIST, getMdsPropVal(kafkaJson, MDSSenderConstants.BOOTSTRAP_SERVERS_LIST));
        mdsSenderProperties.put(MDSSenderConstants.KEY_SERIALIZER_CLASS, getMdsPropVal(kafkaJson, MDSSenderConstants.KEY_SERIALIZER_CLASS));
        mdsSenderProperties.put(MDSSenderConstants.VALUE_SERIALIZER_CLASS, getMdsPropVal(kafkaJson, MDSSenderConstants.VALUE_SERIALIZER_CLASS));
        mdsSenderProperties.put(MDSSenderConstants.PARTITIONER_CLASS, getMdsPropVal(kafkaJson, MDSSenderConstants.PARTITIONER_CLASS));
        mdsSenderProperties.put(MDSSenderConstants.ACKS, getMdsPropVal(kafkaJson, MDSSenderConstants.ACKS));
        mdsSenderProperties.put(MDSSenderConstants.BUFFER_MEMORY, getMdsPropVal(kafkaJson, MDSSenderConstants.BUFFER_MEMORY));
        mdsSenderProperties.put(MDSSenderConstants.DELIVERY_TIMEOUT_MS, getMdsPropVal(kafkaJson, MDSSenderConstants.DELIVERY_TIMEOUT_MS));
        mdsSenderProperties.put(MDSSenderConstants.LINGER_MS, getMdsPropVal(kafkaJson, MDSSenderConstants.LINGER_MS));
        mdsSenderProperties.put(MDSSenderConstants.REQUEST_TIMEOUT_MS, getMdsPropVal(kafkaJson, MDSSenderConstants.REQUEST_TIMEOUT_MS));
        mdsSenderProperties.put(MDSSenderConstants.RETRIES, getMdsPropVal(kafkaJson, MDSSenderConstants.RETRIES));
        mdsSenderProperties.put(MDSSenderConstants.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                getMdsPropVal(kafkaJson, MDSSenderConstants.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION));
        mdsSenderProperties.put(MDSSenderConstants.MAX_REQUEST_SIZE, getMdsPropVal(kafkaJson, MDSSenderConstants.MAX_REQUEST_SIZE));
        mdsSenderProperties.put(MDSSenderConstants.BATCH_SIZE, getMdsPropVal(kafkaJson, MDSSenderConstants.BATCH_SIZE));
        mdsSenderProperties.put(MDSSenderConstants.MAXPRODUCER, getMdsPropVal(kafkaJson, MDSSenderConstants.MAXPRODUCER));
        mdsSenderProperties.put(MDSSenderConstants.MDS_TOPIC, getMdsPropVal(kafkaJson, MDSSenderConstants.MDS_TOPIC));
		
		return mdsSenderProperties;
	}

    /**
     * 获取mds配置信息，如果为空则使用sdk默认配置
     * @param kafkaJson
     * @param propName
     * @return
     */
	private static Object getMdsPropVal(JSONObject kafkaJson,String propName){
        return null==kafkaJson.get(propName)? MdsConfigLoader.getProducerProp().get(propName):kafkaJson.get(propName);
    }

    /**
     * 获取业务场景对应的MDS ID
     *
     * @param mdsns
     * @return
     * @author gucl
     * @ApiDocMethod
     * @ApiCode
     */
    public static final String getMDSId(String mdsns) {
        try {
            if (StringUtil.isBlank(mdsns)) {
                throw new SDKException("命名空间为空，无法获取消息服务ID");
            }
            String conf = getConfigItem(
                    SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取不到消息应用场景对应的MDS服务ID，请检查默认配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String mdsId = data.getString(mdsns);
            if (StringUtil.isBlank(mdsId)) {
                throw new SDKException("从默认配置服务中无法获取消息命名空间[" + mdsns + "]对应的MDS服务ID");
            }
            return mdsId;
        } catch (Exception e) {
            throw new SDKException("获取消息命名空间对应的服务ID错误", e);
        }
    }

    public static Properties assembleMdsConsumerProperties(String namespace) {
        Properties mdsConsumerProperties=new Properties();
//        IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
//        if (configClient == null) {
//            throw new SdkException("cann't get sdkmode mds conf because IConfigClient is null");
//        }
        // 获取mds namespace映射信息
        String mdsNSConfStr="";
        try {
            mdsNSConfStr = getConfigItem(
                    SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]");
        }
        if (StringUtil.isBlank(mdsNSConfStr)) {
            throw new SDKException("cann't get sdkmode mds mdsns conf from path["
                    + SDKConstants.PAAS_MDSNS_MDS_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject mdsNSJson = JSONObject.parseObject(mdsNSConfStr);
        //namespace对应的redis服务标识，如MDS001，MDS002
        String mdsId=mdsNSJson.getString(namespace);
        if(StringUtil.isBlank(mdsId)){
            throw new SDKException("cann't get sdkmoe mdsId of namespace["
                    + namespace + "]");
        }
        // 获取kafka集群配置信息
        String kafkaConfStr="";
        try {
            kafkaConfStr = getConfigItem(SDKConstants.SDK_MODE_PAAS_MDS_CONSUMER_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode mds consumer kafka info from path["
                    + SDKConstants.SDK_MODE_PAAS_MDS_CONSUMER_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode mds consumer kafka info from path["
                    + SDKConstants.SDK_MODE_PAAS_MDS_CONSUMER_MAPPED_PATH + "]");
        }

        if(StringUtil.isBlank(kafkaConfStr)){
            throw new SDKException("cann't get sdkmode mds consumer kafka conf of namespace["
                    + namespace + "],mdsId["+mdsId+"]");
        }

        JSONObject kafkaConfJson = JSONObject.parseObject(kafkaConfStr);
        JSONObject kafkaJson=(JSONObject) kafkaConfJson.get(mdsId);
        mdsConsumerProperties.put(MDSConsumerConstants.BOOTSTRAP_SERVERS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.BOOTSTRAP_SERVERS));
        mdsConsumerProperties.put(MDSConsumerConstants.KEY_DESERIALIZER_CLASS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.KEY_DESERIALIZER_CLASS));
        mdsConsumerProperties.put(MDSConsumerConstants.VALUE_DESERIALIZER_CLASS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.VALUE_DESERIALIZER_CLASS));
        mdsConsumerProperties.put(MDSConsumerConstants.GROUP_ID, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.GROUP_ID));
        mdsConsumerProperties.put(MDSConsumerConstants.AUTO_COMMIT_INTERVAL_MS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.AUTO_COMMIT_INTERVAL_MS));
        mdsConsumerProperties.put(MDSConsumerConstants.AUTO_OFFSET_RESET, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.AUTO_OFFSET_RESET));
        mdsConsumerProperties.put(MDSConsumerConstants.ENABLE_AUTO_COMMIT, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.ENABLE_AUTO_COMMIT));
        mdsConsumerProperties.put(MDSConsumerConstants.FETCH_MAX_WAIT_MS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.FETCH_MAX_WAIT_MS));
        mdsConsumerProperties.put(MDSConsumerConstants.FETCH_MIN_BYTES, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.FETCH_MIN_BYTES));
        mdsConsumerProperties.put(MDSConsumerConstants.MAX_PARTITION_FETCH_BYTES, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.MAX_PARTITION_FETCH_BYTES));
        mdsConsumerProperties.put(MDSConsumerConstants.MAX_POLL_RECORDS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.MAX_POLL_RECORDS));
        mdsConsumerProperties.put(MDSConsumerConstants.PARTITION_ASSIGNMENT_STRATEGY, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.PARTITION_ASSIGNMENT_STRATEGY));
        mdsConsumerProperties.put(MDSConsumerConstants.SESSION_TIMEOUT_MS, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.SESSION_TIMEOUT_MS));
        mdsConsumerProperties.put(MDSConsumerConstants.MDS_TOPIC, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.MDS_TOPIC));
        mdsConsumerProperties.put(MDSConsumerConstants.MDS_CONSUMER_TIMEOUT, getMdsConsumerVal(kafkaJson, MDSConsumerConstants.MDS_CONSUMER_TIMEOUT));

        return mdsConsumerProperties;
    }

    /**
     * 获取mds配置信息，如果为空则使用sdk默认配置
     * @param kafkaJson
     * @param propName
     * @return
     */
    private static Object getMdsConsumerVal(JSONObject kafkaJson,String propName){
        return null==kafkaJson.get(propName)? MdsConfigLoader.getConsumerProp().get(propName):kafkaJson.get(propName);
    }

    public static Properties assembleSesProperties(String namespace) {
        Properties sesProperties=new Properties();
//        IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
//        if (configClient == null) {
//            throw new SdkException("cann't get sdkmode ses conf because IConfigClient is null");
//        }
        // 获取ses namespace映射信息
        String sesNSConfStr="";
        try {
            sesNSConfStr = getConfigItem(
                    SDKConstants.PAAS_SESNS_SES_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode ses sesns conf from path["
                    + SDKConstants.PAAS_SESNS_SES_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode ses sesns conf from path["
                    + SDKConstants.PAAS_SESNS_SES_MAPPED_PATH + "]");
        }
        if (StringUtil.isBlank(sesNSConfStr)) {
            throw new SDKException("cann't get sdkmode ses sesns conf from path["
                    + SDKConstants.PAAS_SESNS_SES_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject sesNSJson = JSONObject.parseObject(sesNSConfStr);
        //namespace对应的redis服务标识，如SES001，SES002
        String sesId=sesNSJson.getString(namespace);
        if(StringUtil.isBlank(sesId)){
            throw new SDKException("cann't get sdkmode sesId of namespace["
                    + namespace + "]");
        }
        // 获取ses集群配置信息
        String sesConfStr="";
        try {
            sesConfStr = getConfigItem(SDKConstants.SDK_MODE_PAAS_SES_ELASTICSEARCH_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode ses elasticsearch info from path["
                    + SDKConstants.SDK_MODE_PAAS_SES_ELASTICSEARCH_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode ses elasticsearch info from path["
                    + SDKConstants.SDK_MODE_PAAS_SES_ELASTICSEARCH_MAPPED_PATH + "]");
        }

        if(StringUtil.isBlank(sesConfStr)){
            throw new SDKException("cann't get sdkmode ses elasticsearch conf of namespace["
                    + namespace + "],sesId["+sesId+"]");
        }

        JSONObject confJson = JSONObject.parseObject(sesConfStr);
        JSONObject json=(JSONObject) confJson.get(sesId);
        if (null == json) {
        	throw new SDKException("无法获取搜索服务[ID: " + sesId + "]对应的配置信息");
        }
        sesProperties.put(SESConsants.ESHOSTS, json.get(SESConsants.ESHOSTS));
        sesProperties.put(SESConsants.INDEXNAME, json.get(SESConsants.INDEXNAME));
        sesProperties.put(SESConsants.MAPPINGID, json.get(SESConsants.MAPPINGID));
        sesProperties.put(SESConsants.MAPPING, json.get(SESConsants.MAPPING));
        sesProperties.put(SESConsants.SHARDS, json.get(SESConsants.SHARDS));
        sesProperties.put(SESConsants.REPLICAS, json.get(SESConsants.REPLICAS));

        return sesProperties;
    }

    public static final String getSESId(String sesns) {
        try {
            if (StringUtil.isBlank(sesns)) {
                throw new SDKException("命名空间为空，无法获取搜索服务服务ID");
            }
            String conf = getConfigItem(
                    SDKConstants.PAAS_SESNS_SES_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取不到搜索服务应用场景对应的SES服务ID，请检查配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String dssId = data.getString(sesns);
            if (StringUtil.isBlank(dssId)) {
                throw new SDKException("从默认配置服务中无法获取搜索服务命名空间[" + sesns + "]对应的SES服务ID");
            }
            return dssId;
        } catch (Exception e) {
            throw new SDKException("获取搜索服务命名空间对应的SES服务ID错误", e);
        }
    }

    public static Properties assembleDssProperties(String namespace) {
        Properties dssProperties=new Properties();
//        IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
//        if (configClient == null) {
//            throw new SDKException("cann't get sdkmode dss conf because IConfigClient is null");
//        }
        // 获取dss namespace映射信息
        String dssNSConfStr="";
        try {
            dssNSConfStr = getConfigItem(
                    SDKConstants.PAAS_DSSNS_DSS_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode dss dssns conf from path["
                    + SDKConstants.PAAS_DSSNS_DSS_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode dss dssns conf from path["
                    + SDKConstants.PAAS_DSSNS_DSS_MAPPED_PATH + "]");
        }
        if (StringUtil.isBlank(dssNSConfStr)) {
            throw new SDKException("cann't get sdkmode dss dssns conf from path["
                    + SDKConstants.PAAS_DSSNS_DSS_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject dssNSJson = JSONObject.parseObject(dssNSConfStr);
        //namespace对应的redis服务标识，如MCS001，MCS002
        String dssId=dssNSJson.getString(namespace);
        if(StringUtil.isBlank(dssId)){
            throw new SDKException("cann't get sdkmoe dssId of namespace["
                    + namespace + "]");
        }
        // 获取dss集群配置信息
        String dssConfStr="";
        try {
            dssConfStr = getConfigItem(SDKConstants.SDK_MODE_PAAS_DSS_MONGO_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode dss mongodb info from path["
                    + SDKConstants.SDK_MODE_PAAS_DSS_MONGO_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode dss mongodb info from path["
                    + SDKConstants.SDK_MODE_PAAS_DSS_MONGO_MAPPED_PATH + "]");
        }

        if(StringUtil.isBlank(dssConfStr)){
            throw new SDKException("cann't get sdkmode dss mongodb conf of namespace["
                    + namespace + "],dssId["+dssId+"]");
        }

        JSONObject redisConfJson = JSONObject.parseObject(dssConfStr);
        JSONObject redisJson=(JSONObject) redisConfJson.get(dssId);
        dssProperties.put(DSSConsants.MONGOSERVER, redisJson.get(DSSConsants.MONGOSERVER));
        dssProperties.put(DSSConsants.DATABASE, redisJson.get(DSSConsants.DATABASE));
        dssProperties.put(DSSConsants.USERNAME, redisJson.get(DSSConsants.USERNAME));
        dssProperties.put(DSSConsants.PASSWORD, redisJson.get(DSSConsants.PASSWORD));
        dssProperties.put(DSSConsants.BUCKET, redisJson.get(DSSConsants.BUCKET));

        return dssProperties;
    }

    public static final String getDSSId(String dssns) {
        try {
            if (StringUtil.isBlank(dssns)) {
                throw new SDKException("命名空间为空，无法获取文档存储服务ID");
            }
            String conf = getConfigItem(
                    SDKConstants.PAAS_DSSNS_DSS_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取不到文档存储应用场景对应的CCS服务ID，请检查默认配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String dssId = data.getString(dssns);
            if (StringUtil.isBlank(dssId)) {
                throw new SDKException("从默认配置服务中无法获取文档存储命名空间[" + dssns + "]对应的DSS服务ID");
            }
            return dssId;
        } catch (Exception e) {
            throw new SDKException("获取文档存储命名空间对应的服务ID错误", e);
        }
    }

    public static Properties assembleMcsProperties(String namespace) {
        Properties mcsProperties=new Properties();
//        IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
//        if (configClient == null) {
//            throw new SdkException("cann't get sdkmode mcs conf because IConfigCenterClient is null");
//        }
        // 获取mcs namespace映射信息
        String cacheNSConfStr="";
        try {
            cacheNSConfStr = getConfigItem(
                    SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode mcs cachens conf from path["
                    + SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode mcs cachens conf from path["
                    + SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH + "]");
        }
        if (StringUtil.isBlank(cacheNSConfStr)) {
            throw new SDKException("cann't get sdkmode mcs cachens conf from path["
                    + SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject cacheNSJson = JSONObject.parseObject(cacheNSConfStr);
        //namespace对应的redis服务标识，如MCS001，MCS002
        String mcsId=cacheNSJson.getString(namespace);
        if(StringUtil.isBlank(mcsId)){
            throw new SDKException("cann't get sdkmoe mcsId of namespace["
                    + namespace + "]");
        }
        // 获取redis集群配置信息
        String redisConfStr="";
        try {
            redisConfStr = getConfigItem(SDKConstants.SDK_MODE_PAAS_MCS_REDIS_MAPPED_PATH);
        } catch (Exception e) {
            LOGGER.error("error！cann't get sdkmode mcs redis info from path["
                    + SDKConstants.SDK_MODE_PAAS_MCS_REDIS_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode mcs redis info from path["
                    + SDKConstants.SDK_MODE_PAAS_MCS_REDIS_MAPPED_PATH + "]");
        }

        if(StringUtil.isBlank(redisConfStr)){
            throw new SDKException("cann't get sdkmode mcs redis conf of namespace["
                    + namespace + "],mcsId["+mcsId+"]");
        }

        JSONObject redisConfJson = JSONObject.parseObject(redisConfStr);
        JSONObject redisJson=(JSONObject) redisConfJson.get(mcsId);
        mcsProperties.put(MCSConstants.MCS_HOST, redisJson.get(MCSConstants.MCS_HOST));
        mcsProperties.put(MCSConstants.MCS_MAXTOTAL, redisJson.get(MCSConstants.MCS_MAXTOTAL));
        mcsProperties.put(MCSConstants.MCS_MAXIDLE, redisJson.get(MCSConstants.MCS_MAXIDLE));
        mcsProperties.put(MCSConstants.MCS_MINIDLE, redisJson.get(MCSConstants.MCS_MINIDLE));
        mcsProperties.put(MCSConstants.MCS_TESTONBORROW, redisJson.get(MCSConstants.MCS_TESTONBORROW));
        mcsProperties.put(MCSConstants.MCS_PASSWORD, redisJson.get(MCSConstants.MCS_PASSWORD));

        return mcsProperties;
    }

    public static final String getServicePwd(String serviceId) {
        try {
            if (StringUtil.isBlank(serviceId)) {
                throw new SDKException("服务ID为空，无法获取服务密码");
            }
            String conf = getConfigItem(
                    SDKConstants.PAAS_SERVICE_PWD_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取的服务标识与密码映射配置为空，请检查默认配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String pwd = data.getString(serviceId);
            if (StringUtil.isBlank(pwd)) {
                throw new SDKException("从默认配置服务中无法获取服务[" + serviceId + "]对应的密码");
            }
            return pwd;
        } catch (Exception e) {
            throw new SDKException("获取服务标识与密码映射配置错误", e);
        }
    }
    public static HikariConfig getDBConf(String dataSourceName) {
        JdbcConfig jdbcConfig = (JdbcConfig) ConfigFactory.getConfig(JdbcConfig.class);
        HikariConfig dbconf = new HikariConfig();
        dbconf.setDriverClassName(jdbcConfig.getDriverClassName());
        dbconf.setJdbcUrl(jdbcConfig.getJdbcUrl());
        dbconf.setUsername(jdbcConfig.getUsername());
        dbconf.setPassword(jdbcConfig.getPassword());
        dbconf.setAutoCommit(jdbcConfig.isAutoCommit());
        dbconf.setConnectionTimeout(jdbcConfig.getConnectionTimeout());
        dbconf.setIdleTimeout(jdbcConfig.getIdleTimeout());
        dbconf.setMaxLifetime(jdbcConfig.getMaxLifetime());
        dbconf.setMaximumPoolSize(jdbcConfig.getMaximumPoolSize());
        return dbconf;
    }

    public static HikariConfig getDBConfByRest(String dataSourceName) {
        Map<String, Object> map = null;
//        if ("1".equalsIgnoreCase(dataSourceName) || StringUtils.isEmpty(dataSourceName)) {
            map = getConfigFile(CcsConstant.JDBC_FILE);
//        } else {
//            map = getConfigFile("jdbc-" + dataSourceName + ".properties");
//        }

        HikariConfig dbconf = new HikariConfig();
        dbconf.setDriverClassName((String) map.get(JdbcConstant.JDBC_DRIVERCLASSNAME));
        dbconf.setJdbcUrl((String) map.get(JdbcConstant.JDBC_JDBCURL));
        dbconf.setUsername((String) map.get(JdbcConstant.JDBC_USERNAME));
        dbconf.setPassword((String) map.get(JdbcConstant.JDBC_PASSWORD));
        dbconf.setAutoCommit(Boolean.parseBoolean((String) map.get(JdbcConstant.JDBC_AUTOCOMMIT)));
        dbconf.setConnectionTimeout(Long.parseLong((String) map
                .get(JdbcConstant.JDBC_CONNECTIONTIMEOUT)));
        dbconf.setIdleTimeout(Long.parseLong((String) map.get(JdbcConstant.JDBC_IDLETIMEOUT)));
        dbconf.setMaxLifetime(Long.parseLong((String) map.get(JdbcConstant.JDBC_MAXLIFETIME)));
        dbconf.setMaximumPoolSize(Integer.parseInt((String) map
                .get(JdbcConstant.JDBC_MAXIMUMPOOLSIZE)));
        dbconf.setConnectionTestQuery((String) map.get(JdbcConstant.JDBC_CONNECTIONTESTQUERY));
        return dbconf;
    }

    public static JSONObject getDubboConfig() {
        DubboConfig dubboConfig = (DubboConfig) ConfigFactory.getConfig(DubboConfig.class);
        return (JSONObject) JSON.toJSON(dubboConfig);
    }

    public static JSONObject getDubboConfigByRest() {
        Map<String, Object> map = getConfigFile(CcsConstant.DUBBO_FILE);
        return (JSONObject) JSON.toJSON(map);
    }

    public static JSONObject getFrameJobJdbcByRest() {
        Map<String, Object> map = getConfigFile(CcsConstant.FRAME_JOB_JDBC_FILE);
        return (JSONObject) JSON.toJSON(map);
    }

    /*
     * public static SsoConfig getSsoConfig(){ SsoConfig ssoConfig = (SsoConfig)
     * ConfigFactory.getConfig(SsoConfig.class); return ssoConfig; }
     */
    public static SsoConfig getSsoConfigByRest() {
        Map<String, Object> map = getConfigFile(CcsConstant.SSO_FILE);
        // SsoConfig ssoConfig = (SsoConfig) ConfigFactory.getConfig(SsoConfig.class);
        SsoConfig ssoConfig = new SsoConfig();
        ssoConfig.setCasServerLoginUrl((String) map.get("cas.server.login.url"));
        ssoConfig.setCasServerUrlPrefix((String) map.get("cas.server.url.prefix"));
        ssoConfig.setServerName((String) map.get("server.name"));
        return ssoConfig;
    }

    public static HdfsConfig getHdfsConfigByRest() {
        Map<String, Object> map = getConfigFile(CcsConstant.HDFS_FILE);
        HdfsConfig config = new HdfsConfig();
        config.setDefaultFS((String) map.get("fs.defaultFS"));
        return config;
    }

    public static void main(String[] args) {
//        System.out.println("====== is" + new Gson().toJson(getSsoConfigByRest()));
        LOGGER.error(getConfigItem("test_key"));
        LOGGER.error(getConfigFile("test.properties").toString());
    }

    /**
     * 获取配置中心配置项
     * 
     * @param path
     * @return
     * @author mayt
     */
    public static String getConfigItem(String path) {
        // ConfigModel configModel = ConfigFactory.getConfigModel();
        // String url = configModel.getConfServerHost() + CcsConstant.CONFIG_URL_ITEM;
        String url = ConfigLoader.getConfServerHost() + CcsConstant.CONFIG_URL_ITEM;
        String result = getConfig(url, path);
        result = JSONObject.parseObject(result).getString("value");
        return result;
    }

    public static Map<String, Object> getConfigFile(String fileName) {
        // ConfigModel configModel = ConfigFactory.getConfigModel();
        // String url = configModel.getConfServerHost() + CcsConstant.CONFIG_URL_FILE;
        String url = ConfigLoader.getConfServerHost() + CcsConstant.CONFIG_URL_FILE;
        String result = getConfig(url, fileName);
        InputStream inputStream = new ByteArrayInputStream(result.getBytes());
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("read config center file error", e);
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, Object> map = new HashMap<String, Object>((Map) properties);
        return map;
    }

    public static Properties assembleIdpsProperties(String namespace) {
        Properties idpsProperties=new Properties();
        IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
        if (configClient == null) {
            throw new SDKException("cann't get sdkmode dss conf because IConfigClient is null");
        }
        // 获取idps namespace映射信息
        String idpsNSConfStr="";
        try {
            idpsNSConfStr = configClient.get(
                    SDKConstants.PAAS_IDPSNS_IDPS_MAPPED_PATH);
        } catch (ConfigException e) {
            LOGGER.error("error！cann't get sdkmode idps idpsns conf from path["
                    + SDKConstants.PAAS_IDPSNS_IDPS_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode idps idpsns conf from path["
                    + SDKConstants.PAAS_IDPSNS_IDPS_MAPPED_PATH + "]");
        }
        if (StringUtil.isBlank(idpsNSConfStr)) {
            throw new SDKException("cann't get sdkmode idps idpsns conf from path["
                    + SDKConstants.PAAS_IDPSNS_IDPS_MAPPED_PATH + "]");
        }
        // 转换为JSON对象
        JSONObject dssNSJson = JSONObject.parseObject(idpsNSConfStr);
        //namespace对应的redis服务标识，如IDPS001，IDPS002
        String idpsId=dssNSJson.getString(namespace);
        if(StringUtil.isBlank(idpsId)){
            throw new SDKException("cann't get sdkmoe idpsId of namespace["
                    + namespace + "]");
        }
        // 获取idps集群配置信息
        String idpsConfStr="";
        try {
            idpsConfStr = configClient.get(SDKConstants.SDK_MODE_PAAS_IDPS_GM_MAPPED_PATH);
        } catch (ConfigException e) {
            LOGGER.error("error！cann't get sdkmode idps gm url info from path["
                    + SDKConstants.SDK_MODE_PAAS_IDPS_GM_MAPPED_PATH + "]",e);
            throw new SDKException("error！cann't get sdkmode idps gm url info from path["
                    + SDKConstants.SDK_MODE_PAAS_IDPS_GM_MAPPED_PATH + "]");
        }

        if(StringUtil.isBlank(idpsConfStr)){
            throw new SDKException("cann't get sdkmode idps gm url conf of namespace["
                    + namespace + "],idpsId["+idpsId+"]");
        }

        JSONObject gmConfJson = JSONObject.parseObject(idpsConfStr);
        JSONObject redisJson=(JSONObject) gmConfJson.get(idpsId);
        idpsProperties.put(IDPSConsants.INTERURL, redisJson.get(IDPSConsants.INTERURL));
        idpsProperties.put(IDPSConsants.INTRAURL, redisJson.get(IDPSConsants.INTRAURL));

        return idpsProperties;
    }

    public static final String getIDPSId(String idpsns) {
        try {
            if (StringUtil.isBlank(idpsns)) {
                throw new SDKException("命名空间为空，无法获取图片服务ID");
            }
            String conf = CCSClientFactory.getDefaultConfigClient().get(
                    SDKConstants.PAAS_IDPSNS_IDPS_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取不到图片应用场景对应的IDPS服务ID，请检查默认配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String dssId = data.getString(idpsns);
            if (StringUtil.isBlank(dssId)) {
                throw new SDKException("从默认配置服务中无法获取图片命名空间[" + idpsns + "]对应的IDPS服务ID");
            }
            return dssId;
        } catch (ConfigException e) {
            throw new SDKException("获取图片命名空间对应的服务ID错误", e);
        }
    }

    private static String getConfig(String url, String path) {
        // ConfigModel configModel = ConfigFactory.getConfigModel();
        // Map<String, String> parameters = new HashMap<>();
        // parameters.put(CcsConstant.APP, configModel.getApp());
        // parameters.put(CcsConstant.VERSION, configModel.getVersion());
        // parameters.put(CcsConstant.ENV, configModel.getEnv());
        // parameters.put(CcsConstant.CONFIG_KEY, path);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(CcsConstant.APP, ConfigLoader.getApp());
        parameters.put(CcsConstant.VERSION, ConfigLoader.getVersion());
        parameters.put(CcsConstant.ENV, ConfigLoader.getEnv());
        parameters.put(CcsConstant.CONFIG_KEY, path);
        String result = HttpUtil.doGet(url, parameters);

        LOGGER.info("获取到path: {} 的配置: {}", path, result);
        return result;
    }

    public static final String getMCSId(String cachens) {
        try {
            if (StringUtil.isBlank(cachens)) {
                throw new SDKException("命名空间为空，无法获取缓存服务ID");
            }
            String conf = getConfigItem(
                    SDKConstants.PAAS_CACHENS_MCS_MAPPED_PATH);
            if (StringUtil.isBlank(conf)) {
                throw new SDKException("获取不到缓存应用场景对应的CCS服务ID，请检查默认配置服务中的相关配置");
            }
            JSONObject data = JSON.parseObject(conf);
            String mcsId = data.getString(cachens);
            if (StringUtil.isBlank(mcsId)) {
                throw new SDKException("从默认配置服务中无法获取缓存命名空间[" + cachens + "]对应的MCS服务ID");
            }
            return mcsId;
        } catch (Exception e) {
            throw new SDKException("获取缓存命名空间对应的服务ID错误", e);
        }
    }
}
