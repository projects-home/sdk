package com.x.sdk.dubbo.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description: 客户消费端获取服务端服务实例<br>
 * Date: 2014年3月27日 <br>
 * 
 */
public final class DubboConsumerFactory {

    private static final String PATH = "dubbo/consumer/dubbo-consumer.xml";

    private static ApplicationContext appContext;

    private DubboConsumerFactory() {

    }

    static {
        DubboPropUtil.setDubboProperties();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { PATH });
        context.start();
        appContext = context;
    }

    public static <T> T getService(String beanId, Class<T> clazz) {
        return getServiceId(beanId, clazz);
    }

    public static <T> T getService(Class<T> clazz) {
        return getServiceId(clazz);
    }

    public static <T> T getService(String beanId) {
        return getServiceId(beanId);
    }

    private static <T> T getServiceId(String beanId, Class<T> clazz) {
        return (T) appContext.getBean(beanId, clazz);
    }

    private static <T> T getServiceId(Class<T> clazz) {
        return (T) appContext.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getServiceId(String beanId) {
        return (T) appContext.getBean(beanId);
    }

}
