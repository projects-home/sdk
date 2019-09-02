package com.x.sdk.test.component.idps;

import com.x.sdk.component.idps.IDPSClientFactory;
import com.x.sdk.component.idps.IImageClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by mayt on 2018/2/9.
 */
public class IImageClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IImageClientTest.class);

    IImageClient iImageClient;
    String idpsns = "sdk-test-idps";
    @Before
    public void setUp() throws Exception {
        iImageClient = IDPSClientFactory.getImageClient(idpsns);
    }

    @After
    public void tearDown() throws Exception {
        iImageClient = null;
    }

    @Test
    public void testUpLoadImage() throws Exception {
        byte[] buffer = null;
        try {
            File file = new File("e:/1.jpg");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String id = iImageClient.upLoadImage(buffer, "666.jpg");
            System.out.println("========= " + id + "==========");
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("", ex);
        }
    }

    @Test
    public void testUpLoadImage1() throws Exception {

    }

    @Test
    public void testGetImgServerInterAddr() throws Exception {

    }

    @Test
    public void testGetImgServerIntraAddr() throws Exception {
    	iImageClient.getImgServerIntraAddr();
    }

    /**
     * 根据图片id 图片类型获取图片内网地址
     * @throws Exception
     */
    @Test
    public void testGetImageUrl() throws Exception {
        String imageId = "5a9a04cca0f3091854341cd5";
		String imageType = ".jpg";
		System.out.println(iImageClient.getImageUrl(imageId, imageType));
    }

    /**
     * 根据图片id 图片类型获取图片内网地址
     * @throws Exception
     */
    @Test
    public void testGetImageUrl1() throws Exception {
        String imageId = "5a9a04cca0f3091854341cd5";
		String imageType = ".jpg";
		String imageScale = "80x80";
		System.out.println(iImageClient.getImageUrl(imageId, imageType, imageScale));
    }

    @Test
    public void testGetImageUploadUrl() throws Exception {

    }

    @Test
    public void testDeleteImage() throws Exception {

    }

    @Test
    public void testGetImageStream() throws Exception {

    }

    @Test
    public void testGetImage() throws Exception {

    }
}