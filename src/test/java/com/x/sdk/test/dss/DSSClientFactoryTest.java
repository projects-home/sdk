package com.x.sdk.test.dss;

import com.x.sdk.dss.DSSClientFactory;
import com.x.sdk.dss.interfaces.IDSSClient;

/**
 * Created by mayt on 2018/1/25.
 */
public class DSSClientFactoryTest {
    public static void main(String[] args) {
        IDSSClient client = DSSClientFactory.getDSSClient("com.x.sdk.dss.test");
        String path = client.insert("mongo text 12");
        long size = client.getFileSize(path);
        System.out.println("path = " + path + ", size = " + size);
    }
}
