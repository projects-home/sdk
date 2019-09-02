package com.x.sdk.component.ccs;


import com.x.sdk.component.ccs.constants.ConfigException;
import com.x.sdk.component.ccs.zookeeper.ConfigWatcher;

import java.util.List;

public interface IConfigClient {

    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    void add(String configPath, String value) throws ConfigException;

    /**
     * @param configPath
     * @param bytes
     * @throws ConfigException
     */
    void add(String configPath, byte[] bytes) throws ConfigException;

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    String get(String configPath) throws ConfigException;

    /**
     * @param configPath
     * @param watcher
     * @return
     * @throws ConfigException
     */
    String get(String configPath, ConfigWatcher watcher) throws ConfigException;

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    byte[] readBytes(String configPath) throws ConfigException;

    /**
     * @param configPath
     * @param watcher
     * @return
     * @throws ConfigException
     */
    byte[] readBytes(String configPath, ConfigWatcher watcher) throws ConfigException;

    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    void modify(String configPath, String value) throws ConfigException;

    /**
     * @param configPath
     * @param value
     * @throws ConfigException
     */
    void modify(String configPath, byte[] value) throws ConfigException;

    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    boolean exists(String configPath) throws ConfigException;
    
    /**
     * @param configPath
     * @return
     * @throws ConfigException
     */
    boolean exists(String configPath, ConfigWatcher watcher) throws ConfigException;

    /**
     * @param configPath
     * @throws ConfigException
     */
    void remove(String configPath) throws ConfigException;

    /**
     * @param configPath
     * @throws ConfigException
     */
    List<String> listSubPath(String configPath) throws ConfigException;

    /**
     * @param configPath
     * @param watcher
     * @throws ConfigException
     */
    List<String> listSubPath(String configPath, ConfigWatcher watcher) throws ConfigException;

}
