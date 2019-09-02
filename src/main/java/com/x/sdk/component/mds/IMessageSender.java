package com.x.sdk.component.mds;

import com.x.sdk.component.mds.vo.BusinessMessage;

public interface IMessageSender {

	/**
	 * 发送一条字符串消息
	 * 
	 * @param msg
	 *            消息体
	 * @param partitionKey
	 *            分区id
	 */
	public void send(String msg, int partitionKey);

	/**
	 * 发送字节消息
	 * 
	 * @param msg
	 *            消息体字节数组
	 * @param partitionKey
	 *            分区id
	 */
	public void send(byte[] msg, int partitionKey);

	/**
	 * 发送一条字符串消息
	 * 
	 * @param msg
	 *            消息体
	 * @param partitionKey
	 *            分区id
	 * @param key
	 *            消息的key
	 */
	public void send(String msg, int partitionKey, String key);

	/**
	 * 发送字节消息
	 * 
	 * @param msg
	 *            消息体字节数组
	 * @param partitionKey
	 *            分区id
	 * @param key
	 *            消息的key
	 */
	public void send(byte[] msg, int partitionKey, String key);

	/**
	 *
	 * @param msg  消息体
	 * @param key  消息的key,根据消息key hash自动分区
	 */
	public void send(String msg, String key);

	/**
	 *
	 * @param msg  消息体字节数组
	 * @param key  消息的key,根据消息key hash自动分区
	 */
	public void send(byte[] msg, String key);

	/**
	 *
	 * @param msg  消息体,消息轮询放入分区
	 */
	public void send(String msg);

	/**
	 *
	 * @param msg  消息体字节数组,消息轮询放入分区
	 */
	public void send(byte[] msg);
	
	/**
	 * 获取分区数量
	 * @return
	 */
	public int getParititions();
	
	void send(BusinessMessage businessMessage, int partitionKey);
	
	void send(BusinessMessage businessMessage, int partitionKey, String key);
}
