package com.x.sdk.test.elasticjob;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MyElasticJob implements SimpleJob {

	@Override
	public void execute(ShardingContext context) {
		
		System.out.println("===["+Thread.currentThread().getName()+"]====context.getShardingItem()="+context.getShardingItem());
		System.out.println("===["+Thread.currentThread().getName()+"]====context="+JSON.toJSONString(context));
		
		switch (context.getShardingItem()) {
	        case 0: 
	            System.out.println("case 0");
	            break;
	        case 1: 
	            System.out.println("case 1");
	            break;
	        case 2: 
	            System.out.println("case 2");
	            break;
	        // case n: ...
		}
	}

}
