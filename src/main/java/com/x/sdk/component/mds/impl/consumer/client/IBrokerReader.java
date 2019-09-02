

package com.x.sdk.component.mds.impl.consumer.client;

public interface IBrokerReader {

	GlobalPartitionInformation getCurrentBrokers();

	void close();
}
