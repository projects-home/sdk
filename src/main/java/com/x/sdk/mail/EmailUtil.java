package com.x.sdk.mail;

import com.x.base.exception.SystemException;
import com.x.sdk.util.CollectionUtil;
import com.x.sdk.util.StringUtil;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    private static Properties prop = new Properties();
    
    private EmailUtil() {
        // 私有构造函数，不运行此类被外部实例化
    }

    static {
        InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream(
                "email/email-conf.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            throw new SystemException("加载email配置出错", e);
        }
    }

    public static HtmlEmail getHtmlEmail() throws EmailException {
        String hostName = prop.getProperty("email.hostname");
        String user = prop.getProperty("email.from.user");
        String password = prop.getProperty("email.from.password");
        String mail = prop.getProperty("email.from.mail");
        String name = prop.getProperty("email.from.name");
        String smtpportStr=prop.getProperty("email.smtpport");
        int smtpport=25;
        if(!StringUtil.isBlank(smtpportStr)){
        	smtpport= Integer.parseInt(smtpportStr);
        }
        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostName);
        email.setAuthentication(user, password);
        email.setFrom(mail, name);
        email.setCharset("utf-8");
        email.setSmtpPort(smtpport);
        return email;
    }

    public static void SendHtmlEmail(String[] tomails, String[] ccmails, String subject,
            String htmlcontext) throws Exception {
        HtmlEmail email = getHtmlEmail();
        email.addTo(tomails);
        if (!CollectionUtil.isEmpty(ccmails)) {
            email.addCc(ccmails);
        }
        email.setSubject(subject);
        email.setHtmlMsg(htmlcontext);
        email.send();
    }

}
