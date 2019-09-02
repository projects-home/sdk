package com.x.sdk.component.mds.constants;

public class MDSSenderConstants {
	/**
	 * bootstrap.servers=172.16.8.27:19092,172.16.8.39:19092,172.16.8.31:19092
	 */
	public static final String BOOTSTRAP_SERVERS_LIST="bootstrap.servers";
	/**
	 * key.serializer=org.apache.kafka.common.serialization.StringSerializer
	 */
	public static final String KEY_SERIALIZER_CLASS="key.serializer";
	/**
	 * serializer.class=org.apache.kafka.common.serialization.StringSerializer
	 */
	public static final String VALUE_SERIALIZER_CLASS="value.serializer";
	/**
	 * partitioner.class=org.apache.kafka.clients.producer.internals.DefaultPartitioner
	 */
	public static final String PARTITIONER_CLASS="partitioner.class";
	/**
	 * acks=1
	 */
	public static final String ACKS="acks";
	/**
	 * buffer.memory=33554432
	 */
	public static final String BUFFER_MEMORY="buffer.memory";
	/**
	 * delivery.timeout.ms=120000
	 */
	public static final String DELIVERY_TIMEOUT_MS = "delivery.timeout.ms";
	/**
	 * linger.ms=0
	 */
	public static final String LINGER_MS="linger.ms";
	/**
	 * request.timeout.ms=30000
	 */
	public static final String REQUEST_TIMEOUT_MS="request.timeout.ms";
	/**
	 * retries=3
	 */
	public static final String RETRIES="retries";
	/**
	 * max.in.flight.requests.per.connection=1
	 */
	public static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION="max.in.flight.requests.per.connection";
	/**
	 * max.request.size=1048576
	 */
	public static final String MAX_REQUEST_SIZE = "max.request.size";
	/**
	 * batch.size=16384
	 */
	public static final String BATCH_SIZE = "batch.size";
	/**
	 * maxProducer=5
	 */
	public static final String MAXPRODUCER="maxProducer";
	/**
	 * mds.topic=bmc_kafka
	 */
	public static final String MDS_TOPIC="mds.topic";

}
