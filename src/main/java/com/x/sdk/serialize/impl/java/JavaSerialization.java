package com.x.sdk.serialize.impl.java;


import com.x.sdk.serialize.ObjectInput;
import com.x.sdk.serialize.ObjectOutput;
import com.x.sdk.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class JavaSerialization implements Serialization {

	public byte getContentTypeId() {
		return 3;
	}

	public String getContentType() {
		return "x-application/java";
	}

	public ObjectOutput serialize(OutputStream out) throws IOException {
		return new JavaObjectOutput(out);
	}

	public ObjectInput deserialize(InputStream is) throws IOException {
		return new JavaObjectInput(is);
	}

}