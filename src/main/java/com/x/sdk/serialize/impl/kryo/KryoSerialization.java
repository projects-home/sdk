package com.x.sdk.serialize.impl.kryo;


import com.x.sdk.serialize.ObjectInput;
import com.x.sdk.serialize.ObjectOutput;
import com.x.sdk.serialize.OptimizedSerialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class KryoSerialization implements OptimizedSerialization {

	public byte getContentTypeId() {
		return 8;
	}

	public String getContentType() {
		return "x-application/kryo";
	}

	public ObjectOutput serialize(OutputStream out) throws IOException {
		return new KryoObjectOutput(out);
	}

	public ObjectInput deserialize(InputStream is) throws IOException {
		return new KryoObjectInput(is);
	}
}