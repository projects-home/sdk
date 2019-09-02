package com.x.sdk.test.ccs;

import com.x.sdk.ccs.util.ConfigTool;

/**
 * Created by mayt on 2018/1/25.
 */
public class ConfigToolTest {
    public static void main(String[] args) {
        System.out.println(ConfigTool.getConfigFile("test.properties"));
        System.out.println(ConfigTool.getConfigItem("/com/x/sdkmode-paas-mcs-redis-mapped"));
    }
}
