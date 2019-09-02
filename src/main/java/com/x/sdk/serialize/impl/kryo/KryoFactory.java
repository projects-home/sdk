package com.x.sdk.serialize.impl.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.x.sdk.serialize.impl.SerializableClassRegistry;
import de.javakaffee.kryoserializers.*;

import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public abstract class KryoFactory {

	// private static final KryoFactory factory = new PrototypeKryoFactory();
	// private static final KryoFactory factory = new SingletonKryoFactory();
	private static final KryoFactory factory = new PooledKryoFactory();

	@SuppressWarnings("rawtypes")
	private final Set<Class> registrations = new LinkedHashSet<Class>();

	private boolean registrationRequired;

	private volatile boolean kryoCreated;

	protected KryoFactory() {
		// Log.DEBUG();
	}

	public static KryoFactory getDefaultFactory() {
		return factory;
	}

	/**
	 * only supposed to be called at startup time
	 *
	 * later may consider adding support for custom serializer, custom id, etc
	 */
	@SuppressWarnings("rawtypes")
	public void registerClass(Class clazz) {

		if (kryoCreated) {
			throw new IllegalStateException(
					"Can't register class after creating kryo instance");
		}
		registrations.add(clazz);
	}

	@SuppressWarnings("rawtypes")
	protected Kryo createKryo() {
		if (!kryoCreated) {
			kryoCreated = true;
		}

		Kryo kryo = new CompatibleKryo();

		// kryo.setReferences(false);
		kryo.setRegistrationRequired(registrationRequired);

		kryo.register(Arrays.asList("").getClass(),
				new ArraysAsListSerializer());
		kryo.register(GregorianCalendar.class,
				new GregorianCalendarSerializer());
		kryo.register(InvocationHandler.class, new JdkProxySerializer());
		kryo.register(BigDecimal.class,
				new DefaultSerializers.BigDecimalSerializer());
		kryo.register(BigInteger.class,
				new DefaultSerializers.BigIntegerSerializer());
		kryo.register(Pattern.class, new RegexSerializer());
		kryo.register(BitSet.class, new BitSetSerializer());
		kryo.register(URI.class, new URISerializer());
		kryo.register(UUID.class, new UUIDSerializer());
		UnmodifiableCollectionsSerializer.registerSerializers(kryo);
		SynchronizedCollectionsSerializer.registerSerializers(kryo);

		// now just added some very common classes
		kryo.register(HashMap.class);
		kryo.register(ArrayList.class);
		kryo.register(LinkedList.class);
		kryo.register(HashSet.class);
		kryo.register(TreeSet.class);
		kryo.register(Hashtable.class);
		kryo.register(Date.class);
		kryo.register(Calendar.class);
		kryo.register(ConcurrentHashMap.class);
		kryo.register(SimpleDateFormat.class);
		kryo.register(GregorianCalendar.class);
		kryo.register(Vector.class);
		kryo.register(BitSet.class);
		kryo.register(StringBuffer.class);
		kryo.register(StringBuilder.class);
		kryo.register(Object.class);
		kryo.register(Object[].class);
		kryo.register(String[].class);
		kryo.register(byte[].class);
		kryo.register(char[].class);
		kryo.register(int[].class);
		kryo.register(float[].class);
		kryo.register(double[].class);

		for (Class clazz : registrations) {
			kryo.register(clazz);
		}

		for (Class clazz : SerializableClassRegistry.getRegisteredClasses()) {
			kryo.register(clazz);
		}

		return kryo;
	}

	public void returnKryo(Kryo kryo) {
		// do nothing by default
	}

	public void setRegistrationRequired(boolean registrationRequired) {
		this.registrationRequired = registrationRequired;
	}

	public void close() {
		// do nothing by default
	}

	public abstract Kryo getKryo();
}
