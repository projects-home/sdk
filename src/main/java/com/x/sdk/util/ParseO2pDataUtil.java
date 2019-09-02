package com.x.sdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.x.sdk.constant.ExceptCodeConstants;

public class ParseO2pDataUtil {

	public static JSONObject getData(String str){
		
		JSONObject json = JSON.parseObject(str);
		String o2pResultCode = json.getString("resultCode");
		/**
		 * 如果o2pResultCode为空代表没有查询到数据
		 * 返回的数据是{'responseHeader':{'resultCode': '000000','resultMessage': 'success','success': true},'data':{}}
		 */
		if(o2pResultCode==null){
			return null;
		}
		/**
		 * 首先查看o2p的resultCode，000000代表成功
		 */
		if(o2pResultCode!=null&& ExceptCodeConstants.Special.SUCCESS.equals(o2pResultCode)){
			/**
			 * 获取长虹侧的data节点
			 */
			JSONObject resultData = (JSONObject) JSON.parse(json.getString("data"));
			JSONObject responseHeader = (JSONObject) JSON.parse(resultData.getString("responseHeader"));
			/**
			 * 获取长虹侧的resultCode节点，000000代表成功
			 */
			String resultCode = responseHeader.getString("resultCode");
			if(resultCode!=null&&ExceptCodeConstants.Special.SUCCESS.equals(resultCode)){
				JSONObject o2pResultData = (JSONObject) JSON.parse(resultData.getString("data"));
				return o2pResultData;
			}
		}
		return (JSONObject) JSON.parse("{resultCode:\""+o2pResultCode+"\"}");
	}
	
}
