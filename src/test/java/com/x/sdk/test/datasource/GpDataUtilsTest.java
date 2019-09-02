package com.x.sdk.test.datasource;

import javax.sql.DataSource;

import com.x.sdk.util.GpDataUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context/core-context.xml" })
public class GpDataUtilsTest {
    @Autowired
    @Qualifier("gpDataSource")
    DataSource ds;

    /**
     * 测试导出150万用户ID需要 5秒到6秒钟
     * 
     * @throws Exception
     * @author panyl
     */
    @Test
    public void testExport() throws Exception {
        long exportNum = GpDataUtils.exportData(ds,
                " select id 学号,name 姓名  from rpt.test2_inner limit 150 ", ",",
                "E:\\temp\\test\\abccd-gbk-header.csv", "GBK", true);
        System.out.println("导出数据成功，行数=" + exportNum);
    }

    /**
     * 测试导入150万用户ID需要 5秒到6秒钟
     * 
     * @throws Exception
     * @author panyl
     */
    @Test
    public void testImport() throws Exception {
        long importNum = GpDataUtils.importData(ds, "rpt.test_export_import", ",",
                "E:\\temp\\test\\abc.txt");
        System.out.println("导入数据成功，行数=" + importNum);
    }

}
