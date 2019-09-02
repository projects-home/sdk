package com.x.sdk.component.ccs.constants;

import com.x.sdk.exception.PaasRuntimeException;

/**
 * Created by astraea on 2015/4/28.
 */
public class ConfigException extends PaasRuntimeException {
	private static final long serialVersionUID = -1348655232003111956L;

	public ConfigException(String errDetail) {
        super(errDetail);
    }

    public ConfigException(String errCode, String errDetail) {
        super(errCode, errDetail);
    }

    public ConfigException(String errCode, Exception ex) {
        super(errCode, ex);
    }

    public ConfigException(String errCode, String errDetail, Exception ex) {
        super(errCode, errDetail, ex);
    }
}
