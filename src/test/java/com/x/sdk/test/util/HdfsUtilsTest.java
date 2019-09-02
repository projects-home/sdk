package com.x.sdk.test.util;

import com.x.sdk.hdfs.utils.HdfsUtils;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

/**
 * HDFS 工具类 Date: 2017年5月17日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author panyl
 */
public class HdfsUtilsTest {

    @Test
    public void copyLocalFile2HDFS() {
        HdfsUtils.copyLocalFile2HDFS("D://home/aaaa.txt", "/test2");
    }

    @Test
    public void copyHDFSToLocalFile() {
        HdfsUtils.copyHDFSToLocalFile("/test2/aaaa.txt", "D:/home");
    }
    
    @Test
    public void checkPathExist() {
    	System.out.println(HdfsUtils.checkPathExist(HdfsUtils.getFileSystem(), "/test2"));
    }
    
    @Test
    public void scanDir() {
    	System.out.println(HdfsUtils.scanDir(HdfsUtils.getFileSystem(), new Path("/test2")));
    }
}
