package com.x.sdk.mail;

import com.x.base.exception.SystemException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class EmailTemplateReader {

    public static String read(String path) {
        String str = reader(path);
        return str;
    }

    private static String reader(String path) {
        SAXReader reader = new SAXReader();
        String str = null;
        try {
            InputStream is = EmailTemplateReader.class.getClassLoader().getResourceAsStream(path);
            Document d = reader.read(is);
            Element e = d.getRootElement();
            Element htmle = e.element("html");
            str = htmle.asXML();
        } catch (DocumentException e) {
            throw new SystemException("解析Email模板出错", e);
        }
        return str;

    }
}
