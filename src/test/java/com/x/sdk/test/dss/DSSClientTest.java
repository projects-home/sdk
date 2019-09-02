package com.x.sdk.test.dss;

import com.x.sdk.dss.DSSClientFactory;
import com.x.sdk.dss.interfaces.IDSSClient;
import org.junit.Test;

import java.io.File;

public class DSSClientTest {
	private String dssns = "com.x.sdk.dss.test";
    @Test
    public void write() throws Exception {
        // 客户资料上传的场景命名空间
    	IDSSClient client=DSSClientFactory.getDSSClient(dssns);
    	File file=new File("e:\\1.jpg");
    	String fileid=client.save(file, "remark");
        System.out.println("文件上传成功，fileid="+fileid);

    }

    @Test
    public void saveAndRead() throws Exception {
    	IDSSClient client= DSSClientFactory.getDSSClient(dssns);
        String ss = "002test\t123456\t6666\t200902190002\t";
        // 客户资料上传的场景命名空间
        // 根据命名空间，从配置中心获取对应的DSS服务ID
        String fileid = "";
        for (int k = 0; k < 10; k++) {
            fileid = client.save((ss+"---"+k).getBytes(), "test");
            System.out.println(fileid);
            byte[] filebytes = client.read(fileid);
            System.out.println(new String(filebytes));
        }
    }
    

}