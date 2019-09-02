package com.x.sdk.config.dubbo;

import com.baidu.disconf.client.common.annotations.DisconfFileItem;

//@Service
//@Scope("singleton")
//@DisconfFile(filename = "dubbo.properties")
public class DubboConfig {
    private String dubboAppname;

    private String dubboRegistryProtocol;

    private String dubboRegistryAddress;

    private String dubboRregistryFile;

    private String dubboProtocol;

    private String dubboProtocolPort;

    private String dubboProviderTimeout;

    private String dubboProtocolService;

    private String dubboProtocolContextpath;

    @DisconfFileItem(name = "dubbo.appname", associateField = "dubboAppname")
    public String getDubboAppname() {
        return dubboAppname;
    }

    public void setAppname(String appname) {
        this.dubboAppname = appname;
    }

    @DisconfFileItem(name = "dubbo.registry.protocol", associateField = "dubboRegistryProtocol")
    public String getDubboRegistryProtocol() {
        return dubboRegistryProtocol;
    }

    public void setRegistryProtocol(String registryProtocol) {
        this.dubboRegistryProtocol = registryProtocol;
    }

    @DisconfFileItem(name = "dubbo.registry.address", associateField = "dubboRegistryAddress")
    public String getDubboRegistryAddress() {
        return dubboRegistryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.dubboRegistryAddress = registryAddress;
    }

    @DisconfFileItem(name = "dubbo.registry.file", associateField = "dubboRregistryFile")
    public String getDubboRegistryFile() {
        return dubboRregistryFile;
    }

    public void setRegistryFile(String registryFile) {
        this.dubboRregistryFile = registryFile;
    }

    @DisconfFileItem(name = "dubbo.protocol", associateField = "dubboProtocol")
    public String getDubboProtocol() {
        return dubboProtocol;
    }

    public void setProtocol(String protocol) {
        this.dubboProtocol = protocol;
    }

    @DisconfFileItem(name = "dubbo.protocol.port", associateField = "dubboProtocolPort")
    public String getDubboProtocolPort() {
        return dubboProtocolPort;
    }

    public void setProtocolPort(String protocolPort) {
        this.dubboProtocolPort = protocolPort;
    }

    @DisconfFileItem(name = "dubbo.provider.timeout", associateField = "dubboProviderTimeout")
    public String getDubboProviderTimeout() {
        return dubboProviderTimeout;
    }

    public void setProviderTimeout(String providerTimeout) {
        this.dubboProviderTimeout = providerTimeout;
    }

    @DisconfFileItem(name = "dubbo.protocol.service", associateField = "dubboProtocolService")
    public String getDubboProtocolService() {
        return dubboProtocolService;
    }

    public void setProtocolService(String protocolService) {
        this.dubboProtocolService = protocolService;
    }

    @DisconfFileItem(name = "dubbo.protocol.contextpath", associateField = "dubboProtocolContextpath")
    public String getDubboProtocolContextpath() {
        return dubboProtocolContextpath;
    }

    public void setProtocolContextpath(String protocolContextpath) {
        this.dubboProtocolContextpath = protocolContextpath;
    }
}
