package com.x.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.base.exception.SystemException;

/**
 * gp数据导入导出工具类
 * 
 * Date: 2017年6月2日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author panyl
 */
public class GpDataUtils {
    private static Logger logger = LoggerFactory.getLogger(GpDataUtils.class);

    /**
     * 
     * @param dataSource
     *            gp数据源，可以spring注入
     * @param sql
     *            sql语句，如 select user_id from rpt.ln_rpt_list_month limit 1500000
     * @param delimiter
     *            列分隔符，如 ,
     * @param filePath
     *            输出文件绝对路径，如"E:\\temp\\test\\abc.txt
     * @param encode
     *            编码
     * @param headers
     *            第一行列头,实现没有做业务校验
     * @return 导出记录行数
     * @author panyl
     */
    public static long exportData(DataSource dataSource, String sql, String delimiter,
            String filePath, String encode, boolean header) {
        Connection con = null;
        OutputStream out = null;
        Writer writer = null;
        try {
            con = dataSource.getConnection();
            CopyManager cm = new CopyManager(con.unwrap(BaseConnection.class));
            StringBuffer sb = new StringBuffer();
            sb.append("copy (");
            sb.append(sql);
            sb.append(" ) TO STDOUT ");
            sb.append("WITH DELIMITER '");
            sb.append(delimiter);
            sb.append("'");
            if (header) {
                sb.append(" HEADER ");
            }
            String copySql = sb.toString();
            logger.error("exportData data begin ,  sql  is {}", copySql);
            long time1 = System.currentTimeMillis();
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file);
            long os = 0;
            if (StringUtils.isNotEmpty(encode)) {// 指定了编码
                writer = new OutputStreamWriter(out, encode);
                os = cm.copyOut(copySql, writer);
            } else {
                os = cm.copyOut(copySql, out);
            }
            logger.error("exportData data end ,  sql  is {},waste time = {}", copySql,
                    System.currentTimeMillis() - time1);
            return os;
        } catch (Exception e) {
            logger.error("导出数据异常", e);
            throw new SystemException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.error("关闭连接异常", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("关闭连接异常", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("关闭连接异常", e);
                }
            }
        }
    }

    /**
     * 
     * @param dataSource
     *            gp数据源，可以spring注入
     * @param table
     *            导入的表名，表必须是已经存在的 如rpt.test_export_import
     * @param delimiter
     *            文件列分隔符，如 ,
     * @param file
     *            输入文件绝对路径， 如 E:\\temp\\test\\abc.txt
     * @return 导入记录行数
     * @author panyl
     */
    public static long importData(DataSource dataSource, String table, String delimiter, String file) {
        Connection con = null;
        InputStream in = null;
        try {
            logger.info("import data begin ");
            con = dataSource.getConnection();
            CopyManager cm = new CopyManager(con.unwrap(BaseConnection.class));
            StringBuffer sb = new StringBuffer();
            sb.append("copy ");
            sb.append(table);
            sb.append(" from STDIN  ");
            sb.append("WITH DELIMITER '");
            sb.append(delimiter);
            sb.append("'");
            String copySql = sb.toString();
            logger.info("import data begin ,  sql  is {}", copySql);
            long time1 = System.currentTimeMillis();
            in = new FileInputStream(file);
            long os = cm.copyIn(copySql, in);
            logger.info("import data end ,  sql  is {},waste time = {}", copySql,
                    System.currentTimeMillis() - time1);
            return os;
        } catch (Exception e) {
            logger.error("导入数据异常", e);
            throw new SystemException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.error("关闭连接异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("关闭连接异常", e);
                }
            }
        }

    }
}
