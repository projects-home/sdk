package com.x.sdk.component.mds.constants;

public class MDSConsumerConstants {
	/**
	 * bootstrap.servers=172.16.8.27:19092,172.16.8.39:19092,172.16.8.31:19092
	 */
	public final static String BOOTSTRAP_SERVERS="bootstrap.servers";
	/**
	 * key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
	 */
	public final static String KEY_DESERIALIZER_CLASS="key.deserializer";
	/**
	 * value.deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer
	 */
	public final static String VALUE_DESERIALIZER_CLASS="value.deserializer";
	/**
	 * group.id=test
	 */
	public final static String GROUP_ID="group.id";
	/**
	 * enable.auto.commit=false
	 */
	public final static String ENABLE_AUTO_COMMIT="enable.auto.commit";
	/**
	 * auto.commit.interval.ms=1000
	 */
	public final static String AUTO_COMMIT_INTERVAL_MS="auto.commit.interval.ms";
	/**
	 * fetch.min.bytes=1
	 */
	public final static String FETCH_MIN_BYTES="fetch.min.bytes";
	/**
	 * fetch.max.wait.ms=500
	 */
	public final static String FETCH_MAX_WAIT_MS="fetch.max.wait.ms";
	/**
	 * max.partition.fetch.bytes=1048576
	 */
	public final static String MAX_PARTITION_FETCH_BYTES="max.partition.fetch.bytes";
	/**
	 * session.timeout.ms=10000
	 */
	public final static String SESSION_TIMEOUT_MS="session.timeout.ms";
	/**
	 * auto.offset.reset=latest
	 */
	public final static String AUTO_OFFSET_RESET = "auto.offset.reset";
	/**
	 * partition.assignment.strategy=org.apache.kafka.clients.consumer.RoundRobinAssignor
	 */
	public final static String PARTITION_ASSIGNMENT_STRATEGY = "partition.assignment.strategy";
	/**
	 * max.poll.records=500
	 */
	public final static String MAX_POLL_RECORDS = "max.poll.records";

	/**
	 * mds.topic=bmc_kafka
	 */
	public final static String MDS_TOPIC="mds.topic";
	/**
	 * mds.consumer.timeout=100
	 */
	public final static String MDS_CONSUMER_TIMEOUT="mds.consumer.timeout";

}
