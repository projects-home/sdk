package com.x.sdk.dubbo.util;

import java.util.Set;

import com.x.sdk.util.ConfigTool;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class DubboPropUtil {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DubboPropUtil.class);

    public static void setDubboProperties() {
//        JSONObject props = ConfigTool.getDubboConfig();
        JSONObject props = ConfigTool.getDubboConfigByRest();
        if (props == null) {
            return;
        }
        LOG.error("获取到的dubbo.properties的配置信息:" + props.toString());
        Set<String> iter = props.keySet();
        for (String key : iter) {
            String value = props.getString(key);
            if (value != null)
                System.setProperty(key.trim(), value);
        }
    }

}
