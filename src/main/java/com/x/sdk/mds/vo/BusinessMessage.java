package com.x.sdk.mds.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BusinessMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String msgID;
	
	private String busiType;
	
	private String msgVersion;
	
	private Timestamp createTime;
	
	private String data;

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(String msgVersion) {
		this.msgVersion = msgVersion;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
