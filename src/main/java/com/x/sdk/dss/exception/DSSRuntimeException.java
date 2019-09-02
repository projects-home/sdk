package com.x.sdk.dss.exception;


import com.x.sdk.exception.PaasRuntimeException;

public class DSSRuntimeException extends PaasRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4661638941581114540L;

	private static final String DSS_MSG = "DSS RUNTIME ERROR";

	public DSSRuntimeException(Exception ex) {
		super(DSS_MSG, ex);
	}

}
