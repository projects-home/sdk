package com.x.sdk.serialize.impl.java;

import java.io.*;

public class CompactedObjectInputStream extends ObjectInputStream {
	private ClassLoader mClassLoader;

	public CompactedObjectInputStream(InputStream in) throws IOException {
		this(in, Thread.currentThread().getContextClassLoader());
	}

	public CompactedObjectInputStream(InputStream in, ClassLoader cl)
			throws IOException {
		super(in);
		mClassLoader = cl == null ? Thread.currentThread()
				.getContextClassLoader() : cl;
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException,
			ClassNotFoundException {
		int type = read();
		if (type < 0)
			throw new EOFException();
		switch (type) {
		case 0:
			return super.readClassDescriptor();
		case 1:
			Class<?> clazz = loadClass(readUTF());
			return ObjectStreamClass.lookup(clazz);
		default:
			throw new StreamCorruptedException(
					"Unexpected class descriptor type: " + type);
		}
	}

	private Class<?> loadClass(String className) throws ClassNotFoundException {
		return mClassLoader.loadClass(className);
	}
}