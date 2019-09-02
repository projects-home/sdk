package com.x.sdk.serialize.impl.java;


import com.x.sdk.serialize.ObjectInput;
import com.x.sdk.serialize.ObjectOutput;
import com.x.sdk.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class CompactedJavaSerialization implements Serialization {

	public byte getContentTypeId() {
		return 4;
	}

	public String getContentType() {
		return "x-application/compactedjava";
	}

	public ObjectOutput serialize(OutputStream out) throws IOException {
		return (ObjectOutput) new JavaObjectOutput(out, true);
	}

	public ObjectInput deserialize(InputStream is) throws IOException {
		return (ObjectInput) new JavaObjectInput(is, true);
	}

}