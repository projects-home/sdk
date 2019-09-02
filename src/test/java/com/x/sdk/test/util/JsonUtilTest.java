package com.x.sdk.test.util;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.x.base.vo.BaseInfo;
import com.x.sdk.util.JsonUtil;

public class JsonUtilTest {
	
	@Ignore
	@Test
	public void testToJson() throws Exception {
		BaseInfo baseInfo = new BaseInfo();
		baseInfo.setTenantId("com");
		baseInfo.setTenantPwd("pwd");
		System.out.println(JsonUtil.toJson(baseInfo));
	}
	
	@Ignore
	@Test
	public void testFromJsonStringClassOfT() {
		fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	public void testFromJsonStringTypeGetterOfT() {
		fail("Not yet implemented");
	}
	
    @Ignore
	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
