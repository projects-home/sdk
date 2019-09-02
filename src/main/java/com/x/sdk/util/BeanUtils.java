package com.x.sdk.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.x.base.exception.SystemException;

/**
 * Description: 提供Bean处理的工具类<br>
 * Date: 2014年2月22日 <br>
 * 
 */
public final class BeanUtils {

    private BeanUtils() {
    }

    /**
     * 拷贝VO对象属性，只拷贝基础属性
     * 
     * @param dest
     * @param orign
     * @
     */
    public static void copyVO(Object destSVO, Object orignSVO) {
        /* 1.源对象与目标对象都不能为空 */
        if (destSVO == null || orignSVO == null) {
            throw new SystemException("拷贝VO属性值出错:源对象为空或目标对象为空");
        }
        /* 2.获取目标对象所有字段 */
        Field[] fields = destSVO.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        /* 3.依次拷贝每个字段取值 */
        for (Field field : fields) {
            String fieldName = field.getName();
            String fieldType = field.getType().getName();
            if (!"serialVersionUID".equals(fieldName)) {
                try {
                    boolean has = hasProperty(orignSVO, fieldName);
                    if (has) {
                        Field orignfield = orignSVO.getClass().getDeclaredField(fieldName);
                        String orignFieldType = orignfield.getType().getName();
                        if (orignFieldType.equals(fieldType)) {
                            Object orignValue = getVoFieldValue(orignSVO, fieldName);
                            setVoFieldValue(destSVO, fieldName, orignValue);
                        }
                    }
                } catch (SecurityException e) {
                    throw new SystemException("拷贝VO属性值出错:SecurityException", e);
                } catch (NoSuchFieldException e) {
                    throw new SystemException("拷贝VO属性值出错:NoSuchFieldException", e);
                } catch (IllegalArgumentException e) {
                    throw new SystemException("拷贝VO属性值出错:IllegalArgumentException", e);
                }

            }
        }
    }

    /**
     * 获取VO指定属性值
     * 
     * @param object
     * @param fieldName
     * @return @
     */
    public static Object getVoFieldValue(Object object, String fieldName) {
        try {
            if (object == null || StringUtil.isBlank(fieldName)) {
                throw new SystemException("底层获取对象指定属性值出错,、对象为空或者指定字段为空");
            }
            String getmethodstr = "get" + setUpperCaseForFirstLetter(fieldName);
            Method getmethod = object.getClass().getMethod(getmethodstr);
            return getmethod.invoke(object);
        } catch (Exception ex) {
            String retMsg = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
            throw new SystemException("系统错误[BeanCopyUtil.getVoFieldValue]:" + retMsg, ex);
        }
    }

    /**
     * 设定VO指定属性值
     * 
     * @param object
     * @param fieldName
     * @param fieldValue
     * @
     */
    public static void setVoFieldValue(Object object, String fieldName, Object fieldValue) {
        try {
            if (object == null || StringUtil.isBlank(fieldName)) {
                throw new SystemException("设置对象指定属性值出错,对象为空或者指定字段为空");
            }
            String setmethodstr = "set" + setUpperCaseForFirstLetter(fieldName);
            String getmethodstr = "get" + setUpperCaseForFirstLetter(fieldName);
            Method getmethod = object.getClass().getMethod(getmethodstr);
            Method setmethod = object.getClass().getMethod(setmethodstr, getmethod.getReturnType());
            setmethod.invoke(object, fieldValue);
        } catch (Exception ex) {
            String retMsg = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
            throw new SystemException("系统错误[BeanCopyUtil.setVoFieldValue]:" + retMsg, ex);
        }
    }

    public static String setUpperCaseForFirstLetter(String name) {
        if (name.length() == 1) {
            return name.toUpperCase();
        }
        String firstLetter = name.substring(0, 1);
        String others = name.substring(1, name.length());
        return firstLetter.toUpperCase() + others;
    }

    /**
     * 对象是否存在指定属性
     * 
     * @param object
     * @param fieldName
     * @return
     */
    public static boolean hasProperty(Object object, String fieldName) {
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return false;
        }
        for (Field field : fields) {
            String fieldN = field.getName();
            if (fieldN.equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据BO字段名（如CHNL_ID）得到VO属性名（如chnlId）
     * 
     * @param bofieldName
     * @return
     */
    public static String getVarName(String bofieldName) {
        bofieldName = bofieldName.toLowerCase();
        char[] objchars = bofieldName.toCharArray();
        for (int j = 0; j < objchars.length; j++) {
            if (objchars[j] == '_' && j != objchars.length - 1) {
                objchars[j + 1] = Character.toUpperCase(objchars[j + 1]);
            }
            if (j == 0) {
                objchars[j] = Character.toLowerCase(objchars[j]);
            }
        }
        String str = String.valueOf(objchars);
        str = str.replaceAll("_", "");
        return str;
    }

    public static Object getFieldValue(Object orignVo, String fieldName) {
        if (orignVo == null || StringUtil.isBlank(fieldName)) {
            throw new SystemException("底层获取vo指定字段值出错,vo对象为空或者指定字段为空");
        }
        try {
            String getMethodName = "get" + toFirstWordUpperCase(fieldName);
            Method getmethod = orignVo.getClass().getDeclaredMethod(getMethodName);
            return getmethod.invoke(orignVo);
        } catch (Exception ex) {
            throw new SystemException("获取属性值报错", ex);
        }
    }

    public static String toFirstWordUpperCase(String key) {
        if ("".equals(key.trim())) {
            key = "";
        } else if (key.length() == 1) {
            key = key.toUpperCase();
        } else {
            key = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
        }
        return key;
    }

    /**
     * 深度拷贝
     * 
     * @param destSVO
     * @param orignSVO
     */
    public static void copyProperties(Object destSVO, Object orignSVO) {
        /* 1.源对象与目标对象都不能为空 */
        if (destSVO == null || orignSVO == null) {
            throw new SystemException("拷贝VO属性值出错:源对象为空或目标对象为空");
        }
        /* 2.深度拷贝 */
        try {
            PropertyUtils.copyProperties(destSVO, orignSVO);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        }
    }

    public static boolean hasMethod(Object obj, String methodName) {
        if (!StringUtil.isBlank(methodName)) {
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * javabean to hashmap
     * 
     * @param bean
     * @return
     */
    public static Map<String, String> toMap(Object bean) {
        Map<String, String> returnMap = new HashMap<String, String>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            throw new SystemException(e);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String propertyName = property.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = property.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(bean);
                } catch (IllegalAccessException e) {
                    throw new SystemException(e);
                } catch (InvocationTargetException e) {
                    throw new SystemException(e);
                }
                if (result != null) {
                    returnMap.put(propertyName, result.toString());
                }
            }
        }
        return returnMap;
    }

    /** map to javabean */
    public static <T> T map2Bean(Map<String, String> map, Class<T> class1) {
        T bean = null;
        try {
            bean = class1.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        }
        return bean;
    }

    public static void map2Bean(Map<String, Object> map, Object bean) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    Method setter = property.getWriteMethod();
                    setter.invoke(bean, value);
                }
            }
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

}
