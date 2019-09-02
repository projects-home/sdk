package com.x.sdk.constant;

public final class JdbcConstant {
    private JdbcConstant() {
    }

    public static final String JDBC_DRIVERCLASSNAME = "jdbc.driverClassName";
    public static final String JDBC_JDBCURL = "jdbc.jdbcUrl";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";
    public static final String JDBC_MAXTOTAL = "jdbc.maxTotal";
    public static final String JDBC_MAXIDLE = "jdbc.maxIdle";
    public static final String JDBC_MAXWAITMILLIS = "jdbc.maxWaitMillis";
    public static final String JDBC_AUTOCOMMIT = "jdbc.autoCommit";
    public static final String JDBC_CONNECTIONTIMEOUT = "jdbc.connectionTimeout";
    public static final String JDBC_IDLETIMEOUT = "jdbc.idleTimeout";
    public static final String JDBC_MAXLIFETIME = "jdbc.maxLifetime";
    public static final String JDBC_MAXIMUMPOOLSIZE = "jdbc.maximumPoolSize";
    
    public static final String JDBC_CONNECTIONTESTQUERY ="jdbc.connectionTestQuery";//gp必须要配置
}
