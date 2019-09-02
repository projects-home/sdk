package com.x.sdk.util;

import com.x.sdk.serialize.ObjectOutput;
import com.x.sdk.serialize.Serialization;
import com.x.sdk.serialize.impl.java.CompactedJavaSerialization;
import com.x.sdk.serialize.impl.java.JavaSerialization;
import com.x.sdk.serialize.impl.kryo.KryoFactory;
import com.x.sdk.serialize.impl.kryo.KryoSerialization;
import com.x.sdk.serialize.impl.nativejava.NativeJavaSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 先根据spring定义的实现来序列化和反序列化 避免放开方法后谁不小心更改了系列化的机制
 * 
 * @author DOUXF
 *
 */
public class SerializeUtil {
	private static transient final Logger log = LoggerFactory.getLogger(SerializeUtil.class);

	private SerializeUtil() {
		//
	}

	private static Serialization javaSer = new JavaSerialization();
	private static Serialization compactedjavaSer = new CompactedJavaSerialization();
	private static Serialization nativejavaSer = new NativeJavaSerialization();
	private static Serialization kryoSer = new KryoSerialization();
	private static Serialization serialization = null;

	private static Serialization getInstance() {
		// 配置谁身上
		if (null == serialization) {
			// 获取系统属性
			String type = System.getProperty("serialization", "kryo");
			switch (type) {
			case "kryo":
				serialization = kryoSer;
				break;
			case "java":
				serialization = javaSer;
				break;
			case "compactedjava":
				serialization = compactedjavaSer;
				break;
			case "nativejava":
				serialization = nativejavaSer;
				break;
			default:
				// kryo
				serialization = kryoSer;
			}
		}
		return serialization;
	}

	@SuppressWarnings("rawtypes")
	public static void register(Class clazz) {
		if (getInstance() instanceof KryoSerialization) {
			KryoFactory.getDefaultFactory().registerClass(clazz);
		}
	}

	public static byte[] serialize(Object object) {
		if (object == null)
			return null;
		if (log.isDebugEnabled()) {
			log.debug(object.getClass() + ":" + object + " transfer into bytes!");
		}
		ByteArrayOutputStream baos = null;
		ObjectOutput objectOutput = null;
		try {
			baos = new ByteArrayOutputStream();
			objectOutput = getInstance().serialize(baos);
			objectOutput.writeObject(object);
			objectOutput.flushBuffer();
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != baos)
					baos.close();
				if (null != objectOutput) {
					objectOutput = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Object deserialize(byte[] bytes) {
		if (bytes == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			Object obj = getInstance().deserialize(bais).readObject();
			if (log.isDebugEnabled()) {
				log.debug("Bytes transfer into :" + obj.getClass() + "," + obj);
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bais)
					bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		SerializeUtil.register(String.class);
	}
}