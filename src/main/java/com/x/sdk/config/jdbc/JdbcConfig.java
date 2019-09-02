package com.x.sdk.config.jdbc;

import com.baidu.disconf.client.common.annotations.DisconfFileItem;

//@Service
//@Scope("singleton")
//@DisconfFile(filename = "jdbc.properties")
public class JdbcConfig {
    private String driverClassName;

    private String jdbcUrl;

    private String username;

    private String password;

    private String maxTotal;

    private String maxIdle;

    private String maxWaitMillis;

    private boolean autoCommit;

    private long connectionTimeout;

    private long idleTimeout;

    private long maxLifetime;

    private int maximumPoolSize;

    @DisconfFileItem(name = "jdbc.driverClassName", associateField = "driverClassName")
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @DisconfFileItem(name = "jdbc.jdbcUrl", associateField = "jdbcUrl")
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @DisconfFileItem(name = "jdbc.username", associateField = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DisconfFileItem(name = "jdbc.password", associateField = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DisconfFileItem(name = "jdbc.maxTotal", associateField = "maxTotal")
    public String getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(String maxTotal) {
        this.maxTotal = maxTotal;
    }

    @DisconfFileItem(name = "jdbc.maxIdle", associateField = "maxIdle")
    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    @DisconfFileItem(name = "jdbc.maxWaitMillis", associateField = "maxWaitMillis")
    public String getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(String maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    @DisconfFileItem(name = "jdbc.autoCommit", associateField = "autoCommit")
    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @DisconfFileItem(name = "jdbc.connectionTimeout", associateField = "connectionTimeout")
    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @DisconfFileItem(name = "jdbc.idleTimeout", associateField = "idleTimeout")
    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    @DisconfFileItem(name = "jdbc.maxLifetime", associateField = "maxLifetime")
    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    @DisconfFileItem(name = "jdbc.maximumPoolSize", associateField = "maximumPoolSize")
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
}
