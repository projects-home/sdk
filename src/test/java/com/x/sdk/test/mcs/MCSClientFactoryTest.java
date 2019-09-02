package com.x.sdk.test.mcs;

import com.x.sdk.component.mcs.MCSClientFactory;
import com.x.sdk.component.mcs.interfaces.ICacheClient;

/**
 * Created by mayt on 2018/1/23.
 */
public class MCSClientFactoryTest {

    public static void main(String[] args) {
        ICacheClient client = MCSClientFactory.getCacheClient("com.x.sdk.mcs.test");
        client.set("test_key", "test_value");
        System.out.println(client.get("test_key"));
    }
}
