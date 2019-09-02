package com.x.sdk.dss.impl;

//import com.ai.paas.ipaas.PaaSConstant;

import com.mongodb.ServerAddress;
import com.x.sdk.constant.PaaSConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DSSHelper {

	public static List<ServerAddress> Str2SAList(String hosts) {
		String[] hostsArray = hosts.split(";|,");
		List<ServerAddress> saList = new ArrayList<>();
		String[] address = null;
		for (String host : hostsArray) {
			address = host.split(":");
			try {
				saList.add(new ServerAddress(address[0], Integer
						.parseInt(address[1])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return saList;
	}

	public static String getFileType(String fileName) {
		String[] fileInfo = fileName.split("\\.");
		return fileInfo[1];
	}

	public static long getFileSize(File file) {
		if (null == file)
			return 0;
		return Long.parseLong(file.length() + "");
	}

	public static long getFileSize(byte[] file) {
		if (null == file)
			return 0;
		return Long.parseLong(file.length + "");
	}

	@SuppressWarnings("rawtypes")
	public static long getListSize(List list) {
		if (null == list)
			return 0;
		long total = 0;
		for (int i = 0; i < list.size(); i++) {
			try {
				total += list.get(i).toString()
						.getBytes(PaaSConstant.CHARSET_UTF8).length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	@SuppressWarnings("rawtypes")
	public static long getSize(Object obj) {
		if (null == obj)
			return 0;
		if (obj instanceof File)
			return getFileSize((File) obj);
		else if (obj instanceof byte[])
			return getFileSize((byte[]) obj);
		else if (obj instanceof List)
			return getListSize((List) obj);
		else
			try {
				return obj.toString().getBytes(PaaSConstant.CHARSET_UTF8).length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return 0;
			}
	}

	public static long okSize(long a, long b) {
		return Long.parseLong((a - b) + "");
	}

	public static byte[] toByteArray(InputStream input) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	public static double byte2M(Long size) {
		BigDecimal bd = new BigDecimal(size);
		return bd.divide(new BigDecimal(1024 * 1024), 3,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static long M2byte(double size) {
		BigDecimal sizeBD = new BigDecimal(size);
		double result = sizeBD.multiply(new BigDecimal(1024 * 1024))
				.doubleValue();
		return Math.round(result);
	}

}
