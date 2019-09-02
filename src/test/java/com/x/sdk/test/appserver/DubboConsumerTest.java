package com.x.sdk.test.appserver;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.base.vo.BaseResponse;
import com.x.sdk.dubbo.util.DubboConsumerFactory;

/**
 * 
 * Date: 2017年6月29日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author panyl
 */
public class DubboConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(DubboConsumerTest.class);

    @Test
    public void test() {
        IDubboSV sv = DubboConsumerFactory.getService(IDubboSV.class);
        System.out.println(11111111);
        long time1 = System.currentTimeMillis();
        try {
            BaseResponse response = sv.testCall();
            System.out.println("ddddddddd=" + (System.currentTimeMillis() - time1));
            System.out.println("response==" + response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

}
