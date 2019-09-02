package com.x.sdk.component.mo;


public class PaasConf {
	
	//========ipaas 服务使用模式 参数  start==========
    /**
     * --ipaas service模式--  认证地址(paasSdkMode=1时有效)
     */
    private String authUrl;

    /**
     * --ipaas service模式--  分配给PaaS层的用户(paasSdkMode=1时有效)
     */
    private String pid;
    
    /**
     * --ipaas service模式--  分配给平台的配置中心服务密码(paasSdkMode=1时有效)
     */
    private String ccsPassword;

    /**
     *  --ipaas service模式--  分配给平台的配置中心地址(paasSdkMode=1时有效)
     */
    private String ccsServiceId;
    //========ipaas 服务使用模式 参数  end==========
    
    
    /**
     * ipaas使用模式 0：以service方式使用   1：以SDK方式使用
     */
  	private String paasSdkMode;
  	/**
  	 * --ipaas service模式--  应用程序名称标识(paasSdkMode=0时有效)
  	 */
    private String ccsAppName;
    /**
     * --ipaas service模式--  zk地址(paasSdkMode=0时有效)
     */
    private String ccsZkAddress;
    //========ipaas SDK使用模式 参数  end==========

    //disconf
//    # 是否使用远程配置文件
//    # true(默认)会从远程获取配置 false则直接获取本地配置
//    enable.remote.conf=true
//            # 配置服务器的 HOST,用逗号分隔  127.0.0.1:8000,127.0.0.1:8000
    private String conf_server_host;
//            # 版本, 请采用 X_X_X_X 格式
private String         version;
//    # APP 请采用 产品线_服务名 格式
private String app;

    public String getConf_server_host() {
        return conf_server_host;
    }

    public void setConf_server_host(String conf_server_host) {
        this.conf_server_host = conf_server_host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    //    # 环境
private String env;
//    # debug
//            debug=true
//    # 忽略哪些分布式配置，用逗号分隔
//            ignore=
//    # 获取远程配置 重试次数，默认是3次
//            conf_server_url_retry_times=1
//    # 获取远程配置 重试时休眠时间，默认是5秒
//            conf_server_url_retry_sleep_seconds=1


    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        /*if (StringUtil.isBlank(authUrl)) {
            throw new SDKException("认证地址为空，请确认是否在paas-conf.properties中是否配置[paas.auth.url]");
        }*/
        this.authUrl = authUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
       /* if (StringUtil.isBlank(pid)) {
            throw new SDKException("config pid is null");
        }*/
        this.pid = pid;
    }

    public String getCcsPassword() {
        return ccsPassword;
    }

    public void setCcsPassword(String ccsPassword) {
       /* if (StringUtil.isBlank(ccsPassword)) {
            throw new SDKException("config service passpord is null");
        }*/
        this.ccsPassword = ccsPassword;
    }

    public String getCcsServiceId() {
        return ccsServiceId;
    }

    public void setCcsServiceId(String ccsServiceId) {
        /*if (StringUtil.isBlank(ccsServiceId)) {
            throw new SDKException("config service Id is null");
        }*/
        this.ccsServiceId = ccsServiceId;
    }

	public String getPaasSdkMode() {
		return paasSdkMode;
	}

	public void setPaasSdkMode(String paasSdkMode) {
		this.paasSdkMode = paasSdkMode;
	}

	public String getCcsAppName() {
		return ccsAppName;
	}

	public void setCcsAppName(String ccsAppName) {
		this.ccsAppName = ccsAppName;
	}

	public String getCcsZkAddress() {
		return ccsZkAddress;
	}

	public void setCcsZkAddress(String ccsZkAddress) {
		this.ccsZkAddress = ccsZkAddress;
	}


}
