package com.x.sdk.component.mds;

public interface IMsgProcessorHandler {
	/**
	 * 需要实现此方法来根据分区数目创建线程数
	 * 
	 * @param partitionNum
	 * @return
	 */
	public IMessageProcessor[] createInstances(int partitionNum);
}
