package com.x.sdk.test.appserver;

import com.alibaba.dubbo.config.annotation.Service;
import com.x.base.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Date: 2017年6月29日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author panyl
 */
@Service
public class DubboProviderSV implements IDubboSV {

	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public BaseResponse testCall() {
	    logger.info("beggin testCall ");
	    long time1 = System.currentTimeMillis();
        try{
            Thread.sleep(10000L);
        }catch(Exception e){
            e.printStackTrace();
        }
        logger.info("ennd testCall waste ={}",(System.currentTimeMillis() - time1));
        return new BaseResponse();
	}



}
