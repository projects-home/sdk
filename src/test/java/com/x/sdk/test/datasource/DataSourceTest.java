package com.x.sdk.test.datasource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context/core-context.xml" })
public class DataSourceTest {

    @Autowired
    @Qualifier("gpDataSource")
    DataSource ds;

    @Autowired
    TestService service;

    @Test
    public void testDataSource1() throws Exception {
        ResultSet rs = ds.getConnection().createStatement()
                .executeQuery("select * from rpt.test2_inner");
        while (rs.next()) {
            System.out.println(rs.getString(2));
        }
    }

    @Test
    public void testExport() throws Exception {
        CopyManager cm = new CopyManager(ds.getConnection().unwrap(BaseConnection.class));
        String sql = "copy (select user_id  from rpt.ln_rpt_list_month limit 1000000 ) TO STDOUT WITH DELIMITER ','";
        System.out.println(sql);
        long time1 = System.currentTimeMillis();
        OutputStream out = new FileOutputStream("E:\\temp\\aaa1000000.txt");
        long os = cm.copyOut(sql, out);
        System.out.println(os + " , time=" + (System.currentTimeMillis() - time1));
    }

    @Test
    public void testImport() throws Exception {
        CopyManager cm = new CopyManager(ds.getConnection().unwrap(BaseConnection.class));
        String sql = "copy  rpt.test_export_import from STDIN WITH  DELIMITER ','";
        System.out.println(sql);
        InputStream in = new FileInputStream("E:\\temp\\aaa1000000.txt");
        long os = cm.copyIn(sql, in);
        System.out.println(os);
    }

    @Test
    public void testDataSource2() {
        int num = service.getGPData();
        System.out.println("访问GP成功，rpt.test2_inner数据量" + num);
    }

}
