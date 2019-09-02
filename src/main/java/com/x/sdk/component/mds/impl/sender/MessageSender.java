package com.x.sdk.component.mds.impl.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.x.sdk.component.mds.IMessageSender;
import com.x.sdk.component.mds.MessageClientException;
import com.x.sdk.constant.PaaSConstant;
import com.x.sdk.mds.vo.BusinessMessage;
import com.x.sdk.util.Assert;
import com.x.sdk.util.DateUtil;

public class MessageSender implements IMessageSender {
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSender.class);
	private Properties cfg = null;
	private String topic = null;
	private List<Producer<String, byte[]>> producerPool = new ArrayList<>();
	private int maxProducer = 0;
	private int paritions = 0;

	public MessageSender(Properties cfg, int maxProducer, String topic) {
		this.cfg = cfg;
		this.topic = topic;
		this.maxProducer = maxProducer;
		// 初始化
		initProducerPool();
	}

	public MessageSender(Properties cfg, int maxProducer, String topic,
			int paritions) {
		this.cfg = cfg;
		this.topic = topic;
		this.maxProducer = maxProducer;
		this.paritions = paritions;
		// 初始化
		initProducerPool();
	}

	private void initProducerPool() {
		if (maxProducer <= 0) {
			maxProducer = 1;
		}
		Producer<String, byte[]> producer = null;
		for (int i = 0; i < maxProducer; i++) {
			producer = new KafkaProducer<String, byte[]>(cfg);
			producerPool.add(producer);
		}
	}

	private Producer<String, byte[]> getProducer() throws Exception {
		int size = producerPool.size();
		return producerPool.get(new Random().nextInt(size));
	}

	private void destoryProducer(Producer<String, byte[]> producer) {
		if (null != producer) {
			try {
				producer.close();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		producerPool.remove(producer);
		producer = new KafkaProducer<String, byte[]>(cfg);
		producerPool.add(producer);
	}

	private void returnResource(Producer<String, byte[]> producer)
			throws Exception {
	}

	@Override
	public void send(String msg, int partitionKey) {
		// 从连接池获取一个实例，进行发送
		Assert.notNull(msg, "The message is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<String, byte[]>(
					topic, partitionKey, null,
					msg.getBytes(PaaSConstant.CHARSET_UTF8));
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(byte[] msg, int partitionKey) {
		// 从连接池获取一个实例，进行发送
		Assert.notNull(msg, "The message is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<String, byte[]>(
					topic, partitionKey, null, msg);
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(String msg, int partitionKey, String key) {
		// 从连接池获取一个实例，进行发送
		Assert.notNull(msg, "The message is null!");
		Assert.notNull(key, "The message key is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<String, byte[]>(
					topic, partitionKey, key,
					msg.getBytes(PaaSConstant.CHARSET_UTF8));
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(byte[] msg, int partitionKey, String key) {
		// 从连接池获取一个实例，进行发送
		Assert.notNull(msg, "The message is null!");
		Assert.notNull(key, "The message key is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<String, byte[]>(
					topic, partitionKey, key, msg);
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(String msg, String key) {
		Assert.notNull(msg, "The message is null!");
		Assert.notNull(key, "The message key is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<>(topic, key,
					msg.getBytes(PaaSConstant.CHARSET_UTF8));
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(byte[] msg, String key) {
		Assert.notNull(msg, "The message is null!");
		Assert.notNull(key, "The message key is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<>(topic, key, msg);
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(String msg) {
		Assert.notNull(msg, "The message is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<>(topic, msg.getBytes(PaaSConstant.CHARSET_UTF8));
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public void send(byte[] msg) {
		Assert.notNull(msg, "The message is null!");
		Producer<String, byte[]> producer = null;
		try {
			producer = getProducer();
			ProducerRecord<String, byte[]> km = new ProducerRecord<>(topic, msg);
			producer.send(km);
		} catch (Exception e) {
			destoryProducer(producer);
			logger.error("", e);
			throw new MessageClientException("MessageSender send:", e);
		} finally {
			if (null != producer) {
				try {
					returnResource(producer);
				} catch (Exception e) {
					logger.error("Can not return massage producer to pool:"
							+ producer, e);
				}
			}
		}
	}

	@Override
	public int getParititions() {
		return paritions;
	}

	@Override
	public void send(BusinessMessage businessMessage, int partitionKey) {
		Assert.notNull(businessMessage, "The message is null!");
		if (null == businessMessage.getCreateTime()) {
			businessMessage.setCreateTime(DateUtil.getSysDate());
		}
		send(JSON.toJSONString(businessMessage), partitionKey);
	}

	@Override
	public void send(BusinessMessage businessMessage, int partitionKey, String key) {
		Assert.notNull(businessMessage, "The message is null!");
		if (null == businessMessage.getCreateTime()) {
			businessMessage.setCreateTime(DateUtil.getSysDate());
		}
		send(JSON.toJSONString(businessMessage), partitionKey, key);
	}

}
