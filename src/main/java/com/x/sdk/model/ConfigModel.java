package com.x.sdk.model;

public class ConfigModel {
	// # 是否使用远程配置文件
	// # true(默认)会从远程获取配置 false则直接获取本地配置
	private boolean enableRemoteConf;

	// #
	// # 配置服务器的 HOST,用逗号分隔 127.0.0.1:8000,127.0.0.1:8000
	// #
	private String confServerHost;
	// #conf_server_host=10.19.15.28:19300
	// #conf_server_host=127.0.0.1:8000

	// # 版本, 请采用 X_X_X_X 格式
	private String version;

	// # APP 请采用 产品线_服务名 格式
	private String app;

	// # 环境
	private String env;

	// # debug
	private boolean debug;

	// # 忽略哪些分布式配置，用逗号分隔
	private String ignore;

	// # 获取远程配置 重试次数，默认是3次
	private String confServerUrlRetryTimes;
	// # 获取远程配置 重试时休眠时间，默认是5秒
	private String confServerUrlRetrySleepSeconds;

	public boolean isEnableRemoteConf() {
		return enableRemoteConf;
	}

	public void setEnableRemoteConf(boolean enableRemoteConf) {
		this.enableRemoteConf = enableRemoteConf;
	}

	public String getConfServerHost() {
		return confServerHost;
	}

	public void setConfServerHost(String confServerHost) {
		this.confServerHost = confServerHost;
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getConfServerUrlRetryTimes() {
		return confServerUrlRetryTimes;
	}

	public void setConfServerUrlRetryTimes(String confServerUrlRetryTimes) {
		this.confServerUrlRetryTimes = confServerUrlRetryTimes;
	}

	public String getConfServerUrlRetrySleepSeconds() {
		return confServerUrlRetrySleepSeconds;
	}

	public void setConfServerUrlRetrySleepSeconds(String confServerUrlRetrySleepSeconds) {
		this.confServerUrlRetrySleepSeconds = confServerUrlRetrySleepSeconds;
	}

}
