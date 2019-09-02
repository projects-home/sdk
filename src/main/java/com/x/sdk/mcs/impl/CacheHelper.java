package com.x.sdk.mcs.impl;

public class CacheHelper {
	
	private static String userId;
	
	public static void setPreKey(String userId) {
		CacheHelper.userId = userId;
	}
	
	public static String preKey() {
		return userId;
	}
}
