package com.x.sdk.config.hdfs;

import com.baidu.disconf.client.common.annotations.DisconfFileItem;

//@Service
//@Scope("singleton")
//@DisconfFile(filename = "hdfs.properties")
public class HdfsConfig {
    private String defaultFS;

    
    public String getDefaultFS() {
        return defaultFS;
    }

    public void setDefaultFS(String defaultFS) {
        this.defaultFS = defaultFS;
    }

}
