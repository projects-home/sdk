package com.x.sdk.component.ccs.client;

import com.x.sdk.component.ccs.IConfigClient;
import com.x.sdk.component.ccs.constants.ConfigException;
import com.x.sdk.component.ccs.zookeeper.ConfigWatcher;
import com.x.sdk.util.ConfigTool;

import java.util.List;

/**
 * Created by mayt on 2018/1/27.
 */
public class DisconfConfigClient implements IConfigClient {
    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    @Override
    public void add(String configPath, String value) throws ConfigException {

    }

    /**
     * @param configPath
     * @param bytes
     * @throws ConfigException
     */
    @Override
    public void add(String configPath, byte[] bytes) throws ConfigException {

    }

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    @Override
    public String get(String configPath) throws ConfigException {
        return ConfigTool.getConfigItem(configPath);
    }

    /**
     * @param configPath
     * @param watcher
     * @return
     * @throws ConfigException
     */
    @Override
    public String get(String configPath, ConfigWatcher watcher) throws ConfigException {
        return null;
    }

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    @Override
    public byte[] readBytes(String configPath) throws ConfigException {
        return new byte[0];
    }

    /**
     * @param configPath
     * @param watcher
     * @return
     * @throws ConfigException
     */
    @Override
    public byte[] readBytes(String configPath, ConfigWatcher watcher) throws ConfigException {
        return new byte[0];
    }

    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    @Override
    public void modify(String configPath, String value) throws ConfigException {

    }

    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    @Override
    public void modify(String configPath, byte[] value) throws ConfigException {

    }

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    @Override
    public boolean exists(String configPath) throws ConfigException {
        return false;
    }

    /**
     * @param configPath
     * @param watcher
     * @return
     * @throws ConfigException
     */
    @Override
    public boolean exists(String configPath, ConfigWatcher watcher) throws ConfigException {
        return false;
    }

    /**
     * @param configPath
     * @throws ConfigException
     */
    @Override
    public void remove(String configPath) throws ConfigException {

    }

    /**
     * @param configPath
     * @throws ConfigException
     */
    @Override
    public List<String> listSubPath(String configPath) throws ConfigException {
        return null;
    }

    /**
     * @param configPath
     * @param watcher
     * @throws ConfigException
     */
    @Override
    public List<String> listSubPath(String configPath, ConfigWatcher watcher) throws ConfigException {
        return null;
    }
}
