package com.x.sdk.datasource;

import com.x.sdk.util.ConfigTool;
import com.zaxxer.hikari.HikariDataSource;

public class XHikariDataSource extends HikariDataSource {

    public XHikariDataSource(String dataSourceName) {
        // 从配置中心获取数据库配置信息，并初始化数据源
//        super(ConfigTool.getDBConf(dataSourceName));
        super(ConfigTool.getDBConfByRest(dataSourceName));
    }

}
