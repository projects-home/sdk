package com.x.sdk.component.idps.impl;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.x.sdk.component.idps.IImageClient;
import com.x.sdk.component.idps.IdpsContant;
import com.x.sdk.component.idps.exception.ImageSizeIllegalException;
import com.x.sdk.component.idps.utils.ImageUtil;
import com.x.sdk.exception.PaasRuntimeException;
import com.x.sdk.util.CiperUtil;
import com.x.sdk.util.StringUtil;

public class ImageClientImpl implements IImageClient {
	private static final Logger log = LoggerFactory.getLogger(ImageClientImpl.class);

	private String pId;
	private String srvId;
	private String srvPwd;
	private String imageUrl;
	private String imageUrlInter;
	private Gson gson = new Gson();

	public ImageClientImpl(String pId, String srvId, String srvPwd, String imageUrl, String imageUrlInter) {
		this.pId = pId;
		this.srvId = srvId;
		this.srvPwd = srvPwd;
		this.imageUrl = imageUrl;
		this.imageUrlInter = imageUrlInter;
	}

	public String upLoadImage(byte[] image, String name) {
		return upLoadImage(image, name, 0, 0);
	}

	public String getImgServerInterAddr() {
		return imageUrlInter;
	}

	public String getImgServerIntraAddr() {
		return imageUrl;
	}

	public InputStream getImageStream(String imageId, String imageType, String imageScale) {
		String downloadUrl = "";
		if (StringUtils.isEmpty(imageScale)) {
			downloadUrl = imageUrl + "/image/" + imageId + imageType;
		} else {
			downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale + imageType;
		}

		log.info("Start to download " + downloadUrl);
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(downloadUrl);

		InputStream in = null;
		try {
			client.executeMethod(httpGet);
			if (200 == httpGet.getStatusCode()) {
				in = httpGet.getResponseBodyAsStream();
				log.info("Successfully download " + downloadUrl);
			}
		} catch (Exception e) {
			log.error("download " + imageId + "." + imageType + ", scale: " + imageScale, e);
		} finally {
			httpGet.releaseConnection();
		}

		return in;
	}

	public boolean deleteImage(String imageId) {
		String deleteUrl = imageUrl + "/deleteImage?imageId=" + imageId;
		return ImageUtil.delImage(deleteUrl, createToken());
	}

	private String imageTypeFormat(String imageType) {
		if (imageType != null && imageType.startsWith(".") == false) {
			imageType = "." + imageType;
		}
		switch (imageType) {
		case ".JPG":
			imageType = ".jpg";
			break;
		case ".PNG":
			imageType = ".png";
			break;
		default:
		}

		return imageType;
	}

	public String getImageUrl(String imageId, String imageType) {
		imageType = imageTypeFormat(imageType);
		return imageUrlInter + "/image/" + imageId + imageType;
	}

	public String getImageUrl(String imageId, String imageType, String imageScale) {
		imageType = imageTypeFormat(imageType);
		if (imageScale != null && imageScale.contains("X")) {
			imageScale = imageScale.replace("X", "x");
		}
		return imageUrlInter + "/image/" + imageId + "_" + imageScale + imageType;
	}

	public String getImageUploadUrl() {
		return imageUrl + "/uploadImage";
	}

	@Override
	public byte[] getImage(String imageId, String imageType, String imageScale) {
		String downloadUrl = "";
		if (StringUtils.isEmpty(imageScale)) {
			downloadUrl = imageUrl + "/image/" + imageId + imageType;
		} else {
			downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale + imageType;
		}

		return ImageUtil.getImage(downloadUrl);
	}

	private String createToken() {
		String token = null;
		if (StringUtil.isBlank(pId))
			return token;
		Gson gson = new Gson();
		JsonObject json = new JsonObject();
		json.addProperty("pid", this.pId);
		json.addProperty("srvId", this.srvId);
		json.addProperty("srvPwd", this.srvPwd);
		String data = gson.toJson(json);
		token = CiperUtil.encrypt(IdpsContant.IDPS_SEC_KEY, data);
		return token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String upLoadImage(byte[] image, String name, int minWidth, int minHeight) {

		if (image == null)
			return null;
		if (image.length > 10 * 1024 * 1024) {
			log.error("upload image size great than 10M of " + name);
			return null;
		}
		String id = null;
		String upUrl = getImageUploadUrl();
		if (upUrl == null || upUrl.length() == 0) {
			log.error("no upload url, pls. check service configration.");
			return null;
		}

		// 上传和删除要加安全验证 ，先简单实现吧，在头上放置用户的pid和服务id，及服务密码的sha1串，在服务端进行验证
		String result = ImageUtil.upImage(upUrl, image, name, minWidth, minHeight, createToken());
		Map<String, String> json;
		json = gson.fromJson(result, Map.class);
		if (null != json && null != json.get("result") && "success".equals(json.get("result"))) {
			id = json.get("id");
		} else {
			// 这里进行异常的处理
			if (null != json && null != json.get("exception")) {
				String exception = json.get("exception");
				if (ImageSizeIllegalException.class.getSimpleName().equalsIgnoreCase(exception)) {
					throw new ImageSizeIllegalException(json.get("message"));
				}
			}
			log.error("result: {}, json: {}", result, json);
			throw new PaasRuntimeException(result);
		}

		return id;
	}

}