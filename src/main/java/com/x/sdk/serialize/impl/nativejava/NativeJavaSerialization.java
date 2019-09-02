package com.x.sdk.serialize.impl.nativejava;


import com.x.sdk.serialize.ObjectInput;
import com.x.sdk.serialize.ObjectOutput;
import com.x.sdk.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class NativeJavaSerialization implements Serialization {

	public static final String NAME = "nativejava";

	public byte getContentTypeId() {
		return 7;
	}

	public String getContentType() {
		return "x-application/nativejava";
	}

	public ObjectOutput serialize(OutputStream output) throws IOException {
		return new NativeJavaObjectOutput(output);
	}

	public ObjectInput deserialize(InputStream input) throws IOException {
		return new NativeJavaObjectInput(input);
	}
}
